package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;

import com.intern.hub.ticket.core.domain.model.EvidenceStatus;

import lombok.Builder;

@Builder
public record EvidenceDto(
                Long evidenceId,
                Long ticketId,
                String evidenceFolder,
                String evidenceUrl,
                OffsetDateTime uploadedAt,
                String fileType,
                Long fileSize,
                EvidenceStatus status) {
}
