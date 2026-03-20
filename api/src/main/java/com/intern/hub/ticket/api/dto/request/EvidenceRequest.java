package com.intern.hub.ticket.api.dto.request;

public record EvidenceRequest(
        String evidenceKey,
        String fileType,
        Long fileSize) {
}
