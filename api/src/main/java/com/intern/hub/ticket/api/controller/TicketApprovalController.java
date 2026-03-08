package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.request.TicketApprovalRequest;
import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;
import com.intern.hub.ticket.core.port.in.ApproveTicketUseCase;
import com.intern.hub.ticket.core.port.in.GetApprovalHistoryUseCase;
import com.intern.hub.ticket.core.port.in.RejectTicketUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}")
@RequiredArgsConstructor
@Transactional
public class TicketApprovalController {

    private final ApproveTicketUseCase approveTicketUseCase;
    private final RejectTicketUseCase rejectTicketUseCase;
    private final GetApprovalHistoryUseCase getApprovalHistoryUseCase;

    @PostMapping("/approve")
    @Authenticated
    public ResponseApi<TicketApprovalDto> approveTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketApprovalRequest request,
            @RequestHeader("X-User-Id") Long approverId) {

        ReviewTicketCommand command = ReviewTicketCommand.builder()
                .ticketId(ticketId)
                .approverId(approverId)
                .comment(request.getComment())
                .build();

        return ResponseApi.ok(approveTicketUseCase.approveTicket(command));
    }

    @PostMapping("/reject")
    @Authenticated
    public ResponseApi<TicketApprovalDto> rejectTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketApprovalRequest request,
            @RequestHeader("X-User-Id") Long approverId) {

        ReviewTicketCommand command = ReviewTicketCommand.builder()
                .ticketId(ticketId)
                .approverId(approverId)
                .comment(request.getComment())
                .build();

        return ResponseApi.ok(rejectTicketUseCase.rejectTicket(command));
    }

    @GetMapping("/approvals")
    @Authenticated
    public ResponseApi<List<TicketApprovalDto>> getApprovalHistory(@PathVariable Long ticketId) {
        return ResponseApi.ok(getApprovalHistoryUseCase.getApprovalHistory(ticketId));
    }
}
