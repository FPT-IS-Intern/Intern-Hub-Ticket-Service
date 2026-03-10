package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;

import lombok.Builder;

@Builder
public record TicketApprovalDto(
        Long approvalId,
        Long ticketId,
        Long approverId,
        TicketApprovalAction action,
        String comment,
        LocalDate actionAt,
        TicketApprovalStatus status) {
}
