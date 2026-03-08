package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record EvidenceDto(
        Long evidenceId,
        Long ticketId,
        String evidenceFolder,
        String evidenceUrl,
        LocalDate uploadedAt,
        String fileType,
        Long fileSize,
        com.intern.hub.ticket.core.domain.model.EvidenceStatus status) {
}
