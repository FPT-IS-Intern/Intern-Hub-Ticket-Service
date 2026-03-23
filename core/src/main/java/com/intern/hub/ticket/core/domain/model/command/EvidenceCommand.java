package com.intern.hub.ticket.core.domain.model.command;

public record EvidenceCommand(
                String tempKey,
                String destinationPath,
                String fileType,
                Long fileSize) {
}
