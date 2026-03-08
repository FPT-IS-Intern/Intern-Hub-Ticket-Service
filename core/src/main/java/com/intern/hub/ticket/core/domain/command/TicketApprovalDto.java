package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record TicketApprovalDto(
        Long approvalId,
        Long ticketId,
        Long approverId,
        com.intern.hub.ticket.core.domain.model.TicketApprovalAction action,
        String comment,
        LocalDate actionAt,
        com.intern.hub.ticket.core.domain.model.TicketApprovalStatus status) {
}
