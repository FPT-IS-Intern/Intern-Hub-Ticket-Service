package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.infra.persistence.entity.TicketTypeEntity;
import com.intern.hub.ticket.infra.persistence.mapper.TicketMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaTicketRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaRepository;
    private final TicketMapper mapper;
    private final EntityManager entityManager;

    @Override
    public Optional<Ticket> findById(Long ticketId) {
        return jpaRepository.findById(ticketId).map(mapper::toDomain);
    }

    @Override
    public List<Ticket> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Ticket> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Ticket> findByStatus(TicketStatus status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Ticket save(Ticket ticket) {
        var entity = mapper.toEntity(ticket);
        if (ticket.getTicketTypeId() != null) {
            entity.setTicketType(entityManager.find(TicketTypeEntity.class, ticket.getTicketTypeId()));
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long ticketId) {
        jpaRepository.deleteById(ticketId);
    }
}
