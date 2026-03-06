package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;
import com.intern.hub.ticket.infra.persistence.mapper.TicketTypeMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaTicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketTypeRepositoryAdapter implements TicketTypeRepository {

    private final JpaTicketTypeRepository jpaRepository;
    private final TicketTypeMapper mapper;

    @Override
    public Optional<TicketType> findById(Long ticketTypeId) {
        return jpaRepository.findById(ticketTypeId).map(mapper::toDomain);
    }

    @Override
    public List<TicketType> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public TicketType save(TicketType ticketType) {
        var entity = mapper.toEntity(ticketType);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long ticketTypeId) {
        jpaRepository.deleteById(ticketTypeId);
    }
}
