package com.intern.hub.ticket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.TicketStatus;
import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

@Repository
public interface JpaTicketRepository extends JpaRepository<TicketEntity, Long> {
    @EntityGraph(attributePaths = { "ticketType" })
    List<TicketEntity> findByUserId(Long userId);

    @EntityGraph(attributePaths = { "ticketType" })
    List<TicketEntity> findByStatus(TicketStatus status);
}
