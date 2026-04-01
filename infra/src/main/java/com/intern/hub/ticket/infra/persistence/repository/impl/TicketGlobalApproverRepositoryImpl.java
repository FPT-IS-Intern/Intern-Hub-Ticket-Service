package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.TicketGlobalApproverModel;
import com.intern.hub.ticket.core.domain.port.TicketGlobalApproverRepository;
import com.intern.hub.ticket.infra.mapper.TicketGlobalApproverMapper;
import com.intern.hub.ticket.infra.persistence.entity.TicketGlobalApprover;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketGlobalApproverJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketGlobalApproverRepositoryImpl implements TicketGlobalApproverRepository {

    private final TicketGlobalApproverJpaRepository jpaRepository;
    private final TicketGlobalApproverMapper mapper;

    @Override
    public Optional<TicketGlobalApproverModel> findByApproverId(Long approverId) {
        return jpaRepository.findById(approverId).map(mapper::toModel);
    }

    @Override
    public List<Long> findApproverIdsByMinLevel(int minLevel) {
        return jpaRepository.findApproverIdsByMinLevel(minLevel);
    }

    @Override
    public TicketGlobalApproverModel save(TicketGlobalApproverModel model) {
        TicketGlobalApprover entity = mapper.toEntity(model);
        return mapper.toModel(jpaRepository.save(entity));
    }

    @Override
    public void deleteByApproverId(Long approverId) {
        jpaRepository.deleteById(approverId);
    }
}

