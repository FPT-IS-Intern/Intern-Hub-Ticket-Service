package com.intern.hub.ticket.api.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.starter.security.annotation.HasPermission;
import com.intern.hub.starter.security.entity.Action;
import com.intern.hub.ticket.api.dto.request.ApproveTicketRequest;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketCommandController {

    private final ApproveTicketUsecase approveTicketUsecase;
    private final CreateTicketUsecase createTicketUsecase;

    @PostMapping
    @Authenticated
    @Transactional
    public ResponseApi<TicketResponse> createTicket(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateTicketRequest request) {

        CreateTicketCommand command = new CreateTicketCommand(userId, request.ticketTypeId(), request.payload());
        TicketModel createdTicket = createTicketUsecase.create(command);

        return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
    }

    @PostMapping("/{ticketId}/approve")
    @HasPermission(action = Action.REVIEW, resource = "ticket")
    @Transactional
    public ResponseApi<?> approveTicket(
            @PathVariable Long ticketId,
            @RequestHeader("X-User-Id") Long approverId,
            @Valid @RequestBody ApproveTicketRequest request) {

        ApproveTicketCommand command = new ApproveTicketCommand(
                ticketId,
                approverId,
                request.comment(),
                request.idempotencyKey());

        approveTicketUsecase.approve(command);

        return ResponseApi.noContent();
    }
}