package com.intern.hub.ticket.infra.persistence.entity;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.EvidenceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "evidences")
public class EvidenceEntity extends BaseAuditEntity {

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
    private LocalDate uploadedAt;

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
