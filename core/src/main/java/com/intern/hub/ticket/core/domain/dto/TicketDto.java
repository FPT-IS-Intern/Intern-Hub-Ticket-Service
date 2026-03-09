package com.intern.hub.ticket.core.domain.dto;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

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
