package com.intern.hub.ticket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByUserId(Long userId);
}
