package com.intern.hub.ticket.core.port.repository;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketType;

public interface TicketTypeRepository {
    Optional<TicketType> findById(Long ticketTypeId);

    List<TicketType> findAll();

    TicketType save(TicketType ticketType);

    void deleteById(Long ticketTypeId);

    Optional<TicketType> findByTypeName(String typeName);

    List<TicketType> findAllById(List<Long> ticketTypeIds);
}
