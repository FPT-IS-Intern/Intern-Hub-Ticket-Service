package com.intern.hub.ticket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

@Repository
public interface JpaTicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByUserId(Long userId);

    List<TicketEntity> findByStatus(String status);
}
