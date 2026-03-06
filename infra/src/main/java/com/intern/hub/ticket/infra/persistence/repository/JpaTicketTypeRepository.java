package com.intern.hub.ticket.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.hub.ticket.infra.persistence.entity.TicketTypeEntity;

public interface JpaTicketTypeRepository extends JpaRepository<TicketTypeEntity, Long> {
}
