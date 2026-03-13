package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.HasPermission;
import com.intern.hub.starter.security.entity.Action;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketDto;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.usecase.GetTicketUsecase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/api/v1")
@RequiredArgsConstructor
public class TicketQueryController {

    private final GetTicketUsecase getTicketUsecase;

    @GetMapping("/all")
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<List<TicketDto>> getAllTickets() {
        List<TicketDto> response = getTicketUsecase.getAllTickets().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

    @GetMapping("/pending")
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<List<TicketDto>> getPendingTickets() {
        List<TicketDto> response = getTicketUsecase.getPendingTickets().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

    @GetMapping("/{ticketId}")
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<TicketDetailDto> getTicketDetail(@PathVariable Long ticketId) {
        TicketModel model = getTicketUsecase.getTicketDetail(ticketId);

        TicketDetailDto detailDto = new TicketDetailDto(
                model.getTicketId(),
                model.getUserId(),
                model.getTicketTypeId(),
                model.getStatus(),
                model.getPayload(),
                model.getCreatedAt(),
                model.getUpdatedAt());
        return ResponseApi.ok(detailDto);
    }

    private TicketDto mapToDto(TicketModel model) {
        return new TicketDto(
                model.getTicketId(),
                model.getUserId(),
                model.getTicketTypeId(),
                model.getStatus(),
                model.getCreatedAt());
    }
}