package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.infra.mapper.TicketApprovalMapper;
import com.intern.hub.ticket.infra.persistence.entity.TicketApproval;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketApprovalJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketApprovalRepositoryImpl implements TicketApprovalRepository {

    private final TicketApprovalJpaRepository jpaRepository;
    private final TicketApprovalMapper mapper;

    @Override
    public TicketApprovalModel save(TicketApprovalModel model) {
        TicketApproval entity = mapper.toEntity(model);
        TicketApproval savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public boolean existsByIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return false;
        }
        return jpaRepository.existsByIdempotencyKey(idempotencyKey);
    }

    @Override
    public Optional<TicketApprovalModel> findByTicketId(Long ticketId) {
        return jpaRepository.findByTicketId(ticketId).map(mapper::toModel);
    }

    @Override
    public List<Object[]> findLatestApproverIdsByTicketIds(List<Long> ticketIds) {
        return jpaRepository.findLatestApproverIdsByTicketIds(ticketIds);
    }
}
