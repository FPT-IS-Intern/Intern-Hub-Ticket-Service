package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;

import lombok.Builder;

@Builder
public record RemoteRequestDto(
                Long ticketId,
                Long workLocationId,
                OffsetDateTime startTime,
                OffsetDateTime endTime,
                String remoteType) {
}
