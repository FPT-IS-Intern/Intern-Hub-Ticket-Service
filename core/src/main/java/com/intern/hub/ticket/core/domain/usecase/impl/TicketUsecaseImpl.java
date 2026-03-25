package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;
import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.RuleEvaluatorPort;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.service.TicketTemplateValidator;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TicketUsecaseImpl implements TicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketEventPublisher ticketEventPublisher;
    private final Snowflake snowflake;
    private final RuleEvaluatorPort ruleEvaluator;
    private final TicketTemplateValidator ticketTemplateValidator;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final EvidenceUsecase evidenceUsecase;

    @Override
    @Transactional(readOnly = true)
    public TicketModel getTicketDetail(Long ticketId) {
        TicketApprovalModel ticketApprovalModel = ticketApprovalRepository.findByTicketId(ticketId)
                .orElse(null);

        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(
                        () -> new NotFoundException("resource.not.found", "Ticket not found with id: " + ticketId));
        if (ticketApprovalModel != null && ticketApprovalModel.getApproverId() != null) {
            ticket.setApproverId(ticketApprovalModel.getApproverId());
        }
        return ticket;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketModel> getPendingTickets() {
        return ticketRepository.findByStatus(TicketStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketModel> getAllTickets(int page, int size) {
        return ticketRepository.findAllPaginated(page, size);
    }

    @Override
    public PaginatedData<TicketModel> getAllTickets(int page, int size, String nameOrEmail, String typeName, String status) {
        return null;
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

        boolean requireEvidence = Boolean.TRUE.equals(ticketType.getRequireEvidence());
        validateEvidences(command.evidences(), requireEvidence);

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

        if (command.evidences() != null && command.evidences().length > 0) {
            String baseDestinationPath = "tickets/" + savedTicket.getTicketId() + "/evidences";
        
            for (MultipartFile file : command.evidences()) {
                if (file == null || file.isEmpty()) {
                    throw new BadRequestException("File is empty or invalid");
                }
        
                Long actorId = snowflake.next();
                evidenceUsecase.uploadFile(file, baseDestinationPath, savedTicket.getTicketId(), actorId);
            }
        }

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
}
