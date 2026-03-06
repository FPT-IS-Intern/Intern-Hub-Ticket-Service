package com.intern.hub.ticket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.hub.ticket.infra.persistence.entity.EvidenceEntity;

public interface JpaEvidenceRepository extends JpaRepository<EvidenceEntity, Long> {
    List<EvidenceEntity> findByTicketId(Long ticketId);
}
