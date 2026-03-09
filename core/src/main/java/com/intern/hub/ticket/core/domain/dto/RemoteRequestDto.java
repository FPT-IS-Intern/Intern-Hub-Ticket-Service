package com.intern.hub.ticket.core.domain.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record RemoteRequestDto(
        Long ticketId,
        Long workLocationId,
        LocalDate startTime,
        LocalDate endTime,
        String remoteType) {
}
