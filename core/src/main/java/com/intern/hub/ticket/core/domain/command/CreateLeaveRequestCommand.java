package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CreateLeaveRequestCommand(
        Long userId,
        Long leaveTypeId,
        LocalDate startAt,
        LocalDate endAt,
        String reason,
        Integer totalDays) {
}
