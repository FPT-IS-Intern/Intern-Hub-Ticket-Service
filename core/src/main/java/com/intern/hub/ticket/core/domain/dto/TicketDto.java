package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

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
        TicketStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long createdBy,
        Long updatedBy) {

    public static OffsetDateTime toOffsetDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return java.time.OffsetDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp),
                java.time.ZoneId.of("UTC"));
    }
}
