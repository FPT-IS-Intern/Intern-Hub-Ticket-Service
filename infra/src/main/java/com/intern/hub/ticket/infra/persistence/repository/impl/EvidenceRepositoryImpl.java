package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.infra.mapper.EvidenceMapper;
import com.intern.hub.ticket.infra.persistence.repository.jpa.EvidenceJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EvidenceRepositoryImpl implements EvidenceRepository {

    private final EvidenceJpaRepository jpaRepository;
    private final EvidenceMapper mapper;

    @Override
    public EvidenceModel save(EvidenceModel model) {
        return mapper.toModel(jpaRepository.save(mapper.toEntity(model))); //
    }

    @Override
    public List<EvidenceModel> findByTicketId(Long ticketId) {
        return mapper.toModels(jpaRepository.findAllByTicketId(ticketId));
    }

    @Override
    public void saveAll(List<EvidenceModel> evidenceEntities) {
        jpaRepository.saveAll(mapper.toEntities(evidenceEntities));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByTicketId(Long ticketId) {
        jpaRepository.deleteAllByTicketId(ticketId);
    }
}
