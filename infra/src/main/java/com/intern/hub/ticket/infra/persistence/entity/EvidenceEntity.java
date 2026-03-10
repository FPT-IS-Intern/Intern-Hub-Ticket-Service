package com.intern.hub.ticket.infra.persistence.entity;

import java.time.OffsetDateTime;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evidences", schema = "ih_ticket")
public class EvidenceEntity extends AuditEntity {

    @Id
    @Column(name = "evidence_id")
    private Long evidenceId;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "evidence_folder")
    private String evidenceFolder;

    @Column(name = "evidence_url")
    private String evidenceUrl;

    @Column(name = "uploaded_at")
    private OffsetDateTime uploadedAt;

    @Column(name = "file_type", length = 100)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private EvidenceStatus status;

    @Version
    @Column(name = "version")
    private Integer version;
}
