package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadEvidenceRequest(

                @NotBlank(message = "Evidence key is required") String evidenceKey,

                @NotBlank(message = "File type is required") String fileType,

                @NotNull(message = "File size is required") Long fileSize) {
}
