package com.intern.hub.ticket.core.domain.port;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;

public interface EvidenceRepository {
    EvidenceModel save(EvidenceModel model);

    List<EvidenceModel> findByTicketId(Long ticketId);

    void saveAll(List<EvidenceModel> evidenceEntities);

    void deleteById(Long id);

    void deleteAllByTicketId(Long ticketId);
}