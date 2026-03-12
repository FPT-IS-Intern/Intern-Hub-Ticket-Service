package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.infra.persistence.entity.Evidence;

@Repository
public interface EvidenceJpaRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findAllByTicketId(Long ticketId);
}
