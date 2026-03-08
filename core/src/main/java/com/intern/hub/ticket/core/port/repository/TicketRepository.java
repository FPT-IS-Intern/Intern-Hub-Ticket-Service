package com.intern.hub.ticket.core.port.repository;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketStatus;

public interface TicketRepository {
    Optional<Ticket> findById(Long ticketId);

    List<Ticket> findAll();

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByStatus(TicketStatus status);

    Ticket save(Ticket ticket);

    void deleteById(Long ticketId);
}
