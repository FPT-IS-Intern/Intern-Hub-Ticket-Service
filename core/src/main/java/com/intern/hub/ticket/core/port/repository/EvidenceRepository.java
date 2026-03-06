package com.intern.hub.ticket.core.port.repository;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.Evidence;

public interface EvidenceRepository {
    Optional<Evidence> findById(Long evidenceId);

    List<Evidence> findByTicketId(Long ticketId);

    Evidence save(Evidence evidence);

    void deleteById(Long evidenceId);
}
