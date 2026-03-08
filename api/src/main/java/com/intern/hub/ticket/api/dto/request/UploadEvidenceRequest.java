package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadEvidenceRequest {

    @NotBlank(message = "Evidence folder cannot be blank")
    private String evidenceFolder;

    @NotBlank(message = "Evidence URL cannot be blank")
    private String evidenceUrl;

    @NotBlank(message = "File type cannot be blank")
    private String fileType;

    @NotNull(message = "File size goes here")
    private Long fileSize;
}
