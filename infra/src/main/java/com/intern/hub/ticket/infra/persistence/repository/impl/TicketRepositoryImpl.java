package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;
import com.intern.hub.ticket.infra.persistence.mapper.TicketMapper;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository jpaRepository;
    private final TicketMapper mapper;

    @Override
    public TicketModel save(TicketModel model) {
        Ticket savedEntity = jpaRepository.save(mapper.toEntity(model));
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<TicketModel> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }
}