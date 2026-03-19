package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;

@Repository
public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByUserId(Long userId);
}
