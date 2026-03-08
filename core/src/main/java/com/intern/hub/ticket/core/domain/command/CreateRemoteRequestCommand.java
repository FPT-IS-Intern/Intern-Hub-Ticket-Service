package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CreateRemoteRequestCommand(
        Long userId,
        LocalDate startAt,
        LocalDate endAt,
        String reason,
        Long workLocationId,
        String remoteType) {
}
