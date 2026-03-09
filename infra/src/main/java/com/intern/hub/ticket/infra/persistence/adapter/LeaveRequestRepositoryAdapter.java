package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.LeaveRequest;
import com.intern.hub.ticket.core.port.repository.LeaveRequestRepository;
import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;
import com.intern.hub.ticket.infra.persistence.mapper.LeaveRequestMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaLeaveRequestRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LeaveRequestRepositoryAdapter implements LeaveRequestRepository {

    private final JpaLeaveRequestRepository jpaRepository;
    private final LeaveRequestMapper mapper;
    private final EntityManager entityManager;

    @Override
    public Optional<LeaveRequest> findByTicketId(Long ticketId) {
        return jpaRepository.findById(ticketId).map(mapper::toDomain);
    }

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        var entity = mapper.toEntity(leaveRequest);
        if (leaveRequest.getTicketId() != null) {
            entity.setTicket(entityManager.getReference(TicketEntity.class, leaveRequest.getTicketId()));
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteByTicketId(Long ticketId) {
        jpaRepository.deleteById(ticketId);
    }
}
