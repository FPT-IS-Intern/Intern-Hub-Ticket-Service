package com.intern.hub.ticket.api.dto.request;

public record EvidenceRequest(
        String tempKey,
        String destinationPath,
        String fileType,
        Long fileSize) {
}
