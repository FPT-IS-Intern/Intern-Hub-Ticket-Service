package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.Builder;

@Builder
public record TicketDto(
        Long ticketId,
        Long userId,
        Long ticketTypeId,
        String ticketTypeName,
        LocalDate startAt,
        LocalDate endAt,
        String reason,
        TicketStatus status) {
}
