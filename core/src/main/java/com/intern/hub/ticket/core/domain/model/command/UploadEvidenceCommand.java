package com.intern.hub.ticket.core.domain.model.command;

public record UploadEvidenceCommand(
        Long ticketId,
        String evidenceFolder,
        String evidenceUrl,
        String fileType,
        Long fileSize) {
}