package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketDto;
import com.intern.hub.ticket.api.dto.response.TicketManagementDto;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4221")
public class TicketQueryController {

    private final TicketUsecase ticketUsecase;
    private final TicketApiMapper mapper;

    /**
     * Lấy tất cả tickets có filter & phân trang.
     *
     * Flow:
     *  1. Nếu có keyword nameOrEmail → gọi HRM Service để tìm userIds
     *  2. Filter tickets bằng userIds (nếu có) + typeName + status
     *  3. Trả về kết quả phân trang
     *
     * @param page        page index (0-based)
     * @param size        page size
     * @param nameOrEmail filter theo fullName hoặc email (tìm qua HRM Service)
     * @param typeName    filter theo tên loại phiếu (LIKE)
     * @param status      filter theo trạng thái (PENDING / APPROVED / REJECTED / CANCELLED)
     */
    @GetMapping("/all")
    public ResponseApi<PaginatedData<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String nameOrEmail,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) String status) {

        PaginatedData<TicketModel> modelPage;
        if (nameOrEmail == null && typeName == null && status == null) {
            // Không có filter → dùng phương thức không gọi HRM (hiệu năng tốt hơn)
            modelPage = ticketUsecase.getAllTickets(page, size);
        } else {
            // Có ít nhất 1 filter → dùng phương thức có gọi HRM nếu nameOrEmail có giá trị
            modelPage = ticketUsecase.getAllTickets(page, size, nameOrEmail, typeName, status);
        }
        return ResponseApi.ok(mapToPaginatedDto(modelPage));
    }

    /**
     * Endpoint cho trang Quản lý phiếu (admin).
     * Tự động gọi HRM Service để lấy fullName, email của TTS.
     * Trả về TicketManagementDto chứa: ticketId, userId, fullName, email, ticketTypeId, typeName, status...
     */
    @GetMapping("/management/all")
    public ResponseApi<PaginatedData<TicketManagementDto>> getAllTicketsForManagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String nameOrEmail,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {

        PaginatedData<TicketModel> modelPage = ticketUsecase.getAllTicketsForManagement(
                page, size, nameOrEmail, typeName, status, startDate, endDate, sortBy, sortDirection);

        return ResponseApi.ok(mapper.toPaginatedManagementDto(modelPage));
    }

    @GetMapping("/pending")
    public ResponseApi<List<TicketDto>> getPendingTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TicketDto> response = ticketUsecase.getPendingTickets().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

    @GetMapping("/{ticketId}")
    public ResponseApi<TicketDetailDto> getTicketDetail(@PathVariable Long ticketId) {
        TicketModel model = ticketUsecase.getTicketDetail(ticketId);

        TicketDetailDto detailDto = new TicketDetailDto(
                model.getTicketId(),
                model.getUserId(),
                model.getTicketTypeId(),
                model.getStatus(),
                model.getPayload(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getCreatedBy(),
                model.getUpdatedBy(),
                model.getRequiredApprovals(),
                model.getCurrentApprovalLevel(),
                model.getApproverId(),
                model.getVersion());

        return ResponseApi.ok(detailDto);
    }

    @GetMapping("/me")
    public ResponseApi<List<TicketDto>> getMyTickets() {
        Long userId = UserContext.requiredUserId();
        List<TicketDto> response = ticketUsecase.getMyTickets(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

    private TicketDto mapToDto(TicketModel model) {
        return new TicketDto(
                model.getTicketId(),
                model.getUserId(),
                model.getTicketTypeId(),
                model.getStatus(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getCreatedBy(),
                model.getUpdatedBy());
    }

    private PaginatedData<TicketDto> mapToPaginatedDto(PaginatedData<TicketModel> modelPage) {
        if (modelPage == null || modelPage.getItems() == null || modelPage.getItems().isEmpty()) {
            return PaginatedData.empty();
        }
        List<TicketDto> dtos = modelPage.getItems().stream().map(this::mapToDto).toList();

        return PaginatedData.<TicketDto>builder()
                .items(dtos)
                .totalItems(modelPage.getTotalItems())
                .totalPages(modelPage.getTotalPages())
                .build();
    }
}
