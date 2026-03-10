package com.intern.hub.ticket.core.domain.model;

import java.time.OffsetDateTime;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Evidence extends AuditEntity {
    private Long evidenceId;
    private Long ticketId;
    private String evidenceFolder;
    private String evidenceUrl;
    private OffsetDateTime uploadedAt;
    private String fileType;
    private Long fileSize;
    private EvidenceStatus status;
    private Integer version;
}
