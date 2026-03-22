package com.intern.hub.ticket.infra.persistence.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "evidences")
@EntityListeners(AuditingEntityListener.class)
public class Evidence extends AuditEntity {

    @Id
    @Column(name = "evidence_id")
    Long id;

    Long ticketId;

    @Column(columnDefinition = "text")
    String evidenceKey;

    @Column(length = 100)
    String fileType;

    Long fileSize;

    @Enumerated(EnumType.STRING)
    EvidenceStatus status;

}
