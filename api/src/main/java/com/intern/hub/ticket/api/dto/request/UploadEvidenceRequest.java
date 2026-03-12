package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadEvidenceRequest(
        @NotBlank(message = "Evidence folder is required") String evidenceFolder,

        @NotBlank(message = "Evidence URL is required") String evidenceUrl,

        @NotBlank(message = "File type is required") String fileType,

        @NotNull(message = "File size is required") Long fileSize) {
}
