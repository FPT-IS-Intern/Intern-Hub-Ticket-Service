package com.intern.hub.ticket.core.domain.command;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import lombok.Builder;

@Builder
public record TicketDto(
        Long ticketId,
        Long userId,
        Long ticketTypeId,
        String ticketTypeName,
        java.time.LocalDate startAt,
        java.time.LocalDate endAt,
        String reason,
        TicketStatus status) {
}
