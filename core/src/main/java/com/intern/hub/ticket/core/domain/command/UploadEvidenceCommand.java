package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadEvidenceCommand {
    private Long ticketId;
    private String evidenceFolder;
    private String evidenceUrl;
    private String fileType;
    private Long fileSize;
}
