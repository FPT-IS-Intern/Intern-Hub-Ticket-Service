package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.EvidenceStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EvidenceDto {
    private Long evidenceId;
    private Long ticketId;
    private String evidenceFolder;
    private String evidenceUrl;
    private LocalDate uploadedAt;
    private String fileType;
    private Long fileSize;
    private EvidenceStatus status;
}
