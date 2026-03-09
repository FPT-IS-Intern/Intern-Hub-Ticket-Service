package com.intern.hub.ticket.core.domain.dto;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.EvidenceStatus;

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
        EvidenceStatus status) {
}
