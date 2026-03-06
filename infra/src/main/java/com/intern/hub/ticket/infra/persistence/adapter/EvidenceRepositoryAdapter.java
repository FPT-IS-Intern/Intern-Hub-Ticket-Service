package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.Evidence;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.infra.persistence.mapper.EvidenceMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaEvidenceRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EvidenceRepositoryAdapter implements EvidenceRepository {

    private final JpaEvidenceRepository jpaRepository;
    private final EvidenceMapper mapper;

    @Override
    public Optional<Evidence> findById(Long evidenceId) {
        return jpaRepository.findById(evidenceId).map(mapper::toDomain);
    }

    @Override
    public List<Evidence> findByTicketId(Long ticketId) {
        return jpaRepository.findByTicketId(ticketId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Evidence save(Evidence evidence) {
        var entity = mapper.toEntity(evidence);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long evidenceId) {
        jpaRepository.deleteById(evidenceId);
    }
}
