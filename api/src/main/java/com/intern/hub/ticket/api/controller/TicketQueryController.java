package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketDto;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TicketQueryController {

    private final TicketUsecase ticketUsecase;

    @GetMapping("/all")
//    @Authenticated
    // @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<PaginatedData<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PaginatedData<TicketModel> modelPage = ticketUsecase.getAllTickets(page, size);
        return ResponseApi.ok(mapToPaginatedDto(modelPage));
    }

    @GetMapping("/pending")
//    @Authenticated
    // @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<List<TicketDto>> getPendingTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TicketDto> response = ticketUsecase.getPendingTickets().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

    @GetMapping("/{ticketId}")
//    @Authenticated
    // @HasPermission(action = Action.READ, resource = "ticket")
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
                model.getApproverId());

        return ResponseApi.ok(detailDto);
    }

    @GetMapping("/me")
//    @Authenticated
    // @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<List<TicketDto>> getMyTickets() {
        //Long userId = 123L;
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