package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record RemoteRequestDto(
        Long ticketId,
        Long workLocationId,
        java.time.LocalDate startTime,
        java.time.LocalDate endTime,
        String remoteType) {
}
