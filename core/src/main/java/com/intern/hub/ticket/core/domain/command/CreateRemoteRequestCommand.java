package com.intern.hub.ticket.core.domain.command;

import java.time.OffsetDateTime;

import lombok.Builder;

@Builder
public record CreateRemoteRequestCommand(
                Long userId,
                OffsetDateTime startAt,
                OffsetDateTime endAt,
                String reason,
                Long workLocationId,
                String remoteType,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt,
                Long createdBy,
                Long updatedBy) {
}
