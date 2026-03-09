package com.intern.hub.ticket.core.domain.command;

import java.time.OffsetDateTime;

import lombok.Builder;

@Builder
public record CreateLeaveRequestCommand(
                Long userId,
                Long leaveTypeId,
                OffsetDateTime startAt,
                OffsetDateTime endAt,
                String reason,
                Integer totalDays) {
}
