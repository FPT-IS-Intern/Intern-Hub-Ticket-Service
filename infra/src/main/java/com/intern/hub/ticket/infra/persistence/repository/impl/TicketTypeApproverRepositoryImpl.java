package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.infra.mapper.TicketTypeApproverMapper;
import com.intern.hub.ticket.infra.persistence.entity.TicketTypeApprover;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketTypeApproverJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketTypeApproverRepositoryImpl implements TicketTypeApproverRepository {

    private final TicketTypeApproverJpaRepository jpaRepository;
    private final TicketTypeApproverMapper mapper;

    @Override
    public boolean exists(Long ticketTypeId, Long approverId) {
        return jpaRepository.existsByTicketTypeIdAndApproverId(ticketTypeId, approverId);
    }

    @Override
    public TicketTypeApproverModel save(TicketTypeApproverModel model) {
        TicketTypeApprover entity = mapper.toEntity(model);
        return mapper.toModel(jpaRepository.save(entity));
    }

    @Override
    public void delete(Long ticketTypeId, Long approverId) {
        jpaRepository.deleteByTicketTypeIdAndApproverId(ticketTypeId, approverId);
    }

    @Override
    public List<Long> findApproverIdsByTicketTypeId(Long ticketTypeId) {
        return jpaRepository.findApproverIdsByTicketTypeId(ticketTypeId);
    }
}
