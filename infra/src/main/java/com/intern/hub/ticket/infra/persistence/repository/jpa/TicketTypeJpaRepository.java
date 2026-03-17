package com.intern.hub.ticket.infra.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.hub.ticket.infra.persistence.entity.TicketType; 

public interface TicketTypeJpaRepository extends JpaRepository<TicketType, Long> {
    boolean existsByTypeNameAndIsDeletedFalse(String name);
}