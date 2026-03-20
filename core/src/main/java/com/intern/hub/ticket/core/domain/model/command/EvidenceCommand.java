package com.intern.hub.ticket.core.domain.model.command;

public record EvidenceCommand(
                String evidenceKey,
                String fileType,
                Long fileSize) {
}
