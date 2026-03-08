package com.intern.hub.ticket.core.domain.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Evidence extends BaseAuditDomain {
    private Long evidenceId;
    private Long ticketId;
    private String evidenceFolder;
    private String evidenceUrl;
    private LocalDate uploadedAt;
    private String fileType;
    private Long fileSize;
    private EvidenceStatus status;
    private Integer version;
}
