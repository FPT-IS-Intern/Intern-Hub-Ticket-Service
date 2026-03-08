package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record UploadEvidenceCommand(
        Long ticketId,
        String evidenceFolder,
        String evidenceUrl,
        String fileType,
        Long fileSize) {
}
