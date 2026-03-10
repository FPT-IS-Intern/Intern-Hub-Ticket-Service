package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;

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
        OffsetDateTime actionAt,
        TicketApprovalStatus status) {
}
