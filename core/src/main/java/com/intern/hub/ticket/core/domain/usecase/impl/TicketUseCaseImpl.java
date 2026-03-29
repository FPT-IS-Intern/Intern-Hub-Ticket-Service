package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.ticket.core.domain.model.*;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;
import com.intern.hub.ticket.core.domain.model.response.ApprovalInfoCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatCardCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatisticsTicketCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.TicketDetailResponse;
import com.intern.hub.ticket.core.domain.port.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.service.TicketTemplateValidator;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketUseCaseImpl implements TicketUsecase {

    TicketRepository ticketRepository;
    TicketTypeRepository ticketTypeRepository;
    TicketEventPublisher ticketEventPublisher;
    Snowflake snowflake;
    RuleEvaluatorPort ruleEvaluator;
    TicketTemplateValidator ticketTemplateValidator;
    TicketApprovalRepository ticketApprovalRepository;
    EvidenceUsecase evidenceUsecase;
    HrmServicePort hrmServicePort;
    InternalUploadDirectPort internalUploadDirectPort;
    EvidenceRepository evidenceRepository;
    static String CONTENT_TYPE_REGEX = "(image/.*|application/pdf|application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document|text/plain)";

    @Override
    @Transactional(readOnly = true)
    public TicketDetailResponse getTicketDetail(Long ticketId) {
        ApprovalInfoCoreResponse ticketApprovalModel = ticketRepository.getApprovalInfo(ticketId);

        if (ticketApprovalModel == null) {
            throw new NotFoundException("not.found", "Ticket approval info not found");
        }

        TicketModel ticket = ticketRepository.getTicketDetail(ticketId);
        if (ticket == null) {
            throw new ConflictDataException("not.found", "Ticket not found");
        }

        // Gọi HRM lấy fullName cho sender và approvers
        String senderFullName = null;
        String approverFullNameLevel1 = null;
        String approverFullNameLevel2 = null;

        HrmUserSearchResponse senderInfo = hrmServicePort.getUserById(ticket.getUserId());
        log.info("[getTicketDetail] ticketId={}, sender userId={}, senderInfo={}", ticketId, ticket.getUserId(), senderInfo);
        if (senderInfo != null) {
            senderFullName = senderInfo.getFullName();
            log.info("[getTicketDetail] sender fullName: {}", senderFullName);
        } else {
            log.warn("[getTicketDetail] senderInfo is null for userId={}", ticket.getUserId());
        }

        if (ticketApprovalModel.getApproverIdLevel1() != null) {
            HrmUserSearchResponse approver1Info = hrmServicePort.getUserById(ticketApprovalModel.getApproverIdLevel1());
            if (approver1Info != null) {
                approverFullNameLevel1 = approver1Info.getFullName();
                log.info("[getTicketDetail] approverLevel1 fullName: {}", approverFullNameLevel1);
            }
        }

        if (ticketApprovalModel.getApproverIdLevel2() != null) {
            HrmUserSearchResponse approver2Info = hrmServicePort.getUserById(ticketApprovalModel.getApproverIdLevel2());
            if (approver2Info != null) {
                approverFullNameLevel2 = approver2Info.getFullName();
                log.info("[getTicketDetail] approverLevel2 fullName: {}", approverFullNameLevel2);
            }
        }

        // Gán fullName vào ticketApprovalModel để map sang DTO
        ticketApprovalModel.setApproverFullNameLevel1(approverFullNameLevel1);
        ticketApprovalModel.setApproverFullNameLevel2(approverFullNameLevel2);
        ticketApprovalModel.setSenderFullName(senderFullName);

        // Gán vào TicketModel để MapStruct map sang TicketDetailDto
        ticket.setSenderFullName(senderFullName);
        ticket.setFullName(senderFullName);
        ticket.setEmail(senderInfo != null ? senderInfo.getEmail() : null);
        ticket.setApproverFullNameLevel1(approverFullNameLevel1);
        ticket.setApproverFullNameLevel2(approverFullNameLevel2);

        return TicketDetailResponse.builder()
                .ticketDetail(ticket)
                .ticketApprovalInfo(ticketApprovalModel)
                .senderFullName(senderFullName)
                .fullName(senderFullName)
                .email(senderInfo != null ? senderInfo.getEmail() : null)
                .approverFullNameLevel1(approverFullNameLevel1)
                .approverFullNameLevel2(approverFullNameLevel2)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketModel> getPendingTickets() {
        return ticketRepository.findByStatus(TicketStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketModel> getAllTickets(int page, int size) {
        PaginatedData<TicketModel> result = ticketRepository.findAllPaginated(page, size);
        if (result.getItems() != null && !result.getItems().isEmpty()) {
            List<TicketModel> tickets = new java.util.ArrayList<>(result.getItems());
            enrichWithUserInfo(tickets);
            enrichWithApproverId(tickets);
            enrichWithApproverFullName(tickets);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketModel> getAllTickets(
            int page,
            int size,
            String nameOrEmail,
            String typeName,
            String status) {

        log.debug("[TicketUsecase] getAllTickets page={}, size={}, nameOrEmail={}, typeName={}, status={}",
                page, size, nameOrEmail, typeName, status);

        List<Long> userIds = Collections.emptyList();

        // Bước 1: Nếu có keyword nameOrEmail → gọi HRM để lấy userIds
        if (nameOrEmail != null && !nameOrEmail.isBlank()) {
            userIds = hrmServicePort.searchUsers(nameOrEmail.trim());
            log.debug("[TicketUsecase] HRM returned {} userIds for keyword='{}'",
                    userIds.size(), nameOrEmail.trim());

            // Edge case: HRM không tìm thấy user nào → tickets = rỗng, không cần query DB
            if (userIds.isEmpty()) {
                log.info("[TicketUsecase] No users found for keyword='{}' → returning empty ticket list",
                        nameOrEmail.trim());
                return PaginatedData.<TicketModel>builder()
                        .items(Collections.emptyList())
                        .totalItems(0L)
                        .totalPages(0)
                        .build();
            }
        }

        // Bước 2: Gọi repository để filter tickets theo userIds (nếu có) + các filter khác
        PaginatedData<TicketModel> result = ticketRepository.findAllPaginated(page, size, userIds, typeName, status);
        if (result.getItems() != null && !result.getItems().isEmpty()) {
            List<TicketModel> tickets = new java.util.ArrayList<>(result.getItems());
            enrichWithUserInfo(tickets);
            enrichWithApproverId(tickets);
            enrichWithApproverFullName(tickets);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketModel create(CreateTicketCommand command) {

        Map<String, Object> safePayload = command.payload() != null ? command.payload() : Collections.emptyMap();

        TicketTypeModel ticketType = ticketTypeRepository.findById(command.ticketTypeId())
                .orElseThrow(() -> new BadRequestException("bad.request", "Ticket type not found"));

        if (Boolean.TRUE.equals(ticketType.getIsDeleted())) {
            throw new BadRequestException("bad.request", "Ticket type is deleted");
        }

        if (ticketType.getFormConfig() != null && !ticketType.getFormConfig().isEmpty()) {
            validatePayloadAgainstTemplate(safePayload, ticketType.getFormConfig());
        }
//
//        boolean requireEvidence = Boolean.TRUE.equals(ticketType.getRequireEvidence());
//        validateEvidences(command.evidences(), requireEvidence);

        int requiredApprovals = 1;
        if (ticketType.getApprovalRule() != null) {
            boolean isTrue = ruleEvaluator.evaluate(ticketType.getApprovalRule().getCondition(), safePayload);
            Integer levels = isTrue ? ticketType.getApprovalRule().getLevelsIfTrue()
                    : ticketType.getApprovalRule().getLevelsIfFalse();
            if (levels != null && levels > 0) {
                requiredApprovals = levels;
            }
        }

        TicketModel ticket = TicketModel.builder()
                .ticketId(snowflake.next())
                .userId(command.userId())
                .ticketTypeId(command.ticketTypeId())
                .status(TicketStatus.PENDING)
                .payload(safePayload)
                .requiredApprovals(requiredApprovals)
                .currentApprovalLevel(1)
                .isDeleted(false)
                .build();

        TicketModel savedTicket = ticketRepository.save(ticket);

//        if (command.evidences() != null && command.evidences().length > 0) {
//            String destinationPath = "tickets/evidences/" + savedTicket.getTicketId();
//            long actorId;
//            for (MultipartFile evidence : command.evidences()) {
//                if (evidence == null || evidence.isEmpty()) {
//                    continue;
//                }
//                actorId = snowflake.next();
//                String objectKey = internalUploadDirectPort.uploadFile(
//                        evidence,
//                        destinationPath + evidence.getOriginalFilename(),
//                        actorId,
//                        20971520L,
//                        CONTENT_TYPE_REGEX);
//
//                evidenceRepository.save(
//                        new EvidenceModel(
//                                snowflake.next(),
//                                savedTicket.getTicketId(),
//                                objectKey,
//                                evidence.getContentType(),
//                                evidence.getSize(),
//                                EvidenceStatus.UPLOADED,
//                                0
//                        )
//                );
//            }
//        }

        ticketEventPublisher.publishTicketCreatedEvent(savedTicket);

        return savedTicket;
    }

    private void validateEvidences(MultipartFile[] evidences, boolean requireEvidence) {
        if (requireEvidence && (evidences == null || evidences.length == 0)) {
            throw new BadRequestException("bad.request", "Evidence is required");
        }
        if (evidences == null || evidences.length == 0) {
            return;
        }
        for (MultipartFile evidence : evidences) {
            if (evidence == null || evidence.isEmpty()) {
                throw new BadRequestException("bad.request", "Evidence file must not be empty");
            }
        }
    }

    private void validatePayloadAgainstTemplate(Map<String, Object> payload, List<TicketTemplateField> template) {

        Map<String, Object> safePayload = payload != null ? payload : new HashMap<>();

        Set<String> validFieldCodes = template.stream()
                .map(TicketTemplateField::getFieldCode)
                .collect(Collectors.toSet());

        for (String payloadKey : safePayload.keySet()) {
            if (!validFieldCodes.contains(payloadKey)) {
                throw new BadRequestException("invalid.field", "Payload contains unknown field: " + payloadKey);
            }
        }

        for (TicketTemplateField field : template) {
            String fieldCode = field.getFieldCode();
            Object value = safePayload.get(fieldCode);

            if (field.isRequired() && isNullOrEmpty(value)) {
                throw new BadRequestException("bad.request", "Missing or empty required field: " + fieldCode);
            }

            if (!isNullOrEmpty(value)) {
                ticketTemplateValidator.validateFieldType(field, value);
            }
        }
    }

    private boolean isNullOrEmpty(Object value) {
        return value == null || (value instanceof String && ((String) value).trim().isEmpty());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<TicketModel> getMyTickets(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketModel> getAllTicketsForManagement(
            int page,
            int size,
            String nameOrEmail,
            String typeName,
            String status,
            Long startDate,
            Long endDate,
            String sortBy,
            String sortDirection) {

        log.debug("[TicketUsecase] getAllTicketsForManagement page={}, size={}, nameOrEmail={}, typeName={}, status={}, startDate={}, endDate={}, sortBy={}, sortDirection={}",
                page, size, nameOrEmail, typeName, status, startDate, endDate, sortBy, sortDirection);

        List<Long> userIds = Collections.emptyList();

        if (nameOrEmail != null && !nameOrEmail.isBlank()) {
            userIds = hrmServicePort.searchUsers(nameOrEmail.trim());
            if (userIds.isEmpty()) {
                log.info("[TicketUsecase] No users found for keyword='{}' → returning empty ticket list",
                        nameOrEmail.trim());
                return PaginatedData.<TicketModel>builder()
                        .items(Collections.emptyList())
                        .totalItems(0L)
                        .totalPages(0)
                        .build();
            }
        }

        PaginatedData<TicketModel> result = ticketRepository.findAllPaginated(
                page, size, userIds, typeName, status, startDate, endDate, sortBy, sortDirection);
        List<TicketModel> tickets = new java.util.ArrayList<>(result.getItems());

        if (!tickets.isEmpty()) {
            enrichWithUserInfo(tickets);
            enrichWithTypeName(tickets);
            enrichWithApproverId(tickets);
            enrichWithApproverFullName(tickets);
        }

        return result;
    }

    private void enrichWithUserInfo(List<TicketModel> tickets) {
        List<Long> userIds = tickets.stream()
                .map(TicketModel::getUserId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        if (userIds.isEmpty()) {
            return;
        }

        Map<Long, HrmUserSearchResponse> userMap = hrmServicePort.getUsersByIds(userIds);

        tickets.forEach(ticket -> {
            HrmUserSearchResponse user = userMap.get(ticket.getUserId());
            if (user != null) {
                log.info("[get full name] fullname: {}", user.getFullName());
                log.info("[get email] email: {}", user.getEmail());
                ticket.setFullName(user.getFullName());
                ticket.setEmail(user.getEmail());
            }
        });
    }

    private void enrichWithTypeName(List<TicketModel> tickets) {
        List<Long> typeIds = tickets.stream()
                .map(TicketModel::getTicketTypeId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        if (typeIds.isEmpty()) {
            return;
        }

        List<com.intern.hub.ticket.core.domain.model.TicketTypeModel> types =
                ticketTypeRepository.findAllByIds(typeIds);

        Map<Long, com.intern.hub.ticket.core.domain.model.TicketTypeModel> typeMap = types.stream()
                .collect(Collectors.toMap(com.intern.hub.ticket.core.domain.model.TicketTypeModel::getTicketTypeId, t -> t, (a, b) -> a));

        tickets.forEach(ticket -> {
            com.intern.hub.ticket.core.domain.model.TicketTypeModel type = typeMap.get(ticket.getTicketTypeId());
            if (type != null) {
                ticket.setTypeName(type.getTypeName());
            }
        });
    }

    private void enrichWithApproverId(List<TicketModel> tickets) {
        List<Long> ticketIds = tickets.stream()
                .map(TicketModel::getTicketId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        if (ticketIds.isEmpty()) {
            return;
        }

        List<Object[]> approverRows = ticketApprovalRepository.findLatestApproverIdsByTicketIds(ticketIds);

        Map<Long, Long> approverMap = approverRows.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> row[1] != null ? ((Number) row[1]).longValue() : null,
                        (a, b) -> a
                ));

        tickets.forEach(ticket -> {
            Long approverId = approverMap.get(ticket.getTicketId());
            if (approverId != null) {
                ticket.setApproverId(approverId);
            }
        });
    }

    /**
     * Enrich tickets với approverFullName bằng cách batch-fetch thông tin approver từ HRM.
     * Chỉ gọi nếu ticket đã có approverId (sau khi enrichWithApproverId chạy).
     */
    private void enrichWithApproverFullName(List<TicketModel> tickets) {
        List<Long> approverIds = tickets.stream()
                .map(TicketModel::getApproverId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        if (approverIds.isEmpty()) {
            return;
        }

        Map<Long, HrmUserSearchResponse> approverMap = hrmServicePort.getUsersByIds(approverIds);

        tickets.forEach(ticket -> {
            if (ticket.getApproverId() != null) {
                HrmUserSearchResponse approver = approverMap.get(ticket.getApproverId());
                if (approver != null) {
                    log.info("[get full name] fullname: {}", approver.getFullName());
                    ticket.setApproverFullName(approver.getFullName());
                }
            }
        });
    }

    @Override
    public StatCardCoreResponse getStatCardData() {
        return ticketRepository.getStatCardData();
    }

    @Override
    public StatisticsTicketCoreResponse statisticsTicket() {
        List<Long> userIds = hrmServicePort.getAllUserId();
        return ticketRepository.statisticsTicket(userIds);
    }
}
