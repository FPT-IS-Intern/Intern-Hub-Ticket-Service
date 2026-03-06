package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.TicketApproval;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.infra.persistence.mapper.TicketApprovalMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaTicketApprovalRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketApprovalRepositoryAdapter implements TicketApprovalRepository {

    private final JpaTicketApprovalRepository jpaRepository;
    private final TicketApprovalMapper mapper;

    @Override
    public Optional<TicketApproval> findById(Long approvalId) {
        return jpaRepository.findById(approvalId).map(mapper::toDomain);
    }

    @Override
    public List<TicketApproval> findByTicketId(Long ticketId) {
        return jpaRepository.findByTicketId(ticketId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public TicketApproval save(TicketApproval ticketApproval) {
        var entity = mapper.toEntity(ticketApproval);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long approvalId) {
        jpaRepository.deleteById(approvalId);
    }
}
