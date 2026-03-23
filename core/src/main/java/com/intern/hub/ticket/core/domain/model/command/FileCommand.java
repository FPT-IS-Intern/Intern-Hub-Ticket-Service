package com.intern.hub.ticket.core.domain.model.command;

/**
 * Pure Java command model for file upload.
 * Replaces MultipartFile at the core layer to maintain clean architecture
 * (core layer must not depend on Spring framework).
 */
public record FileCommand(
        byte[] content,
        String originalFilename,
        String contentType,
        Long size) {

    public String getExtension() {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    }
}
