package com.intern.hub.ticket.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.request.ApproveTicketRequest;
import com.intern.hub.ticket.api.dto.request.BulkApproveTicketRequest;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveResponse;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketCommandController {

    private final ApproveTicketUsecase approveTicketUsecase;
    private final CreateTicketUsecase createTicketUsecase;

    @PostMapping
    @Authenticated
    public ResponseApi<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request) {

        // Long userId = UserContext.requiredUserId();
        Long userId = 123L;

        CreateTicketCommand command = new CreateTicketCommand(userId, request.ticketTypeId(), request.payload());
        TicketModel createdTicket = createTicketUsecase.create(command);

        return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
    }

    @PostMapping("/{ticketId}/approve")
    @Authenticated
    // @HasPermission(action = Action.REVIEW, resource = "ticket")
    public ResponseApi<?> approveTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody ApproveTicketRequest request) {

        // Long approverId = UserContext.requiredUserId();
        Long approverId = 123L;

        ApproveTicketCommand command = new ApproveTicketCommand(
                ticketId,
                approverId,
                request.comment(),
                request.idempotencyKey(),
                request.version());

        approveTicketUsecase.approve(command);

        return ResponseApi.noContent();
    }

    @PostMapping("/{ticketId}/reject")
    @Authenticated
    // @HasPermission(action = Action.REVIEW, resource = "ticket")
    public ResponseApi<?> rejectTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody ApproveTicketRequest request) {

        // Long approverId = UserContext.requiredUserId();
        Long approverId = 123L;

        RejectTicketCommand command = new RejectTicketCommand(
                ticketId,
                approverId,
                request.comment(),
                request.idempotencyKey(),
                request.version());

        approveTicketUsecase.reject(command);

        return ResponseApi.noContent();
    }

    @PostMapping("/bulk-approve")
    @Authenticated
    // @HasPermission(action = Action.REVIEW, resource = "ticket")
    public ResponseApi<BulkApproveResponse> bulkApprove(
            @Valid @RequestBody BulkApproveTicketRequest request) {

        // Long approverId = UserContext.requiredUserId();
        Long approverId = 123L;

        BulkApproveTicketCommand command = new BulkApproveTicketCommand(
                request.idempotencyKey(),
                request.tickets(),
                approverId,
                request.comment());
        BulkApproveResponse response = approveTicketUsecase.bulkApprove(command);

        return ResponseApi.ok(response);
    }

}