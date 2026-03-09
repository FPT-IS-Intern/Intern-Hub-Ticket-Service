package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import lombok.Builder;

@Builder
public record TicketDto(
                Long ticketId,
                Long userId,
                Long ticketTypeId,
                String ticketTypeName,
                OffsetDateTime startAt,
                OffsetDateTime endAt,
                String reason,
                TicketStatus status) {
}
