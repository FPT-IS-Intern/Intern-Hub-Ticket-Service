package com.intern.hub.ticket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.hub.ticket.infra.persistence.entity.TicketApprovalEntity;

public interface JpaTicketApprovalRepository extends JpaRepository<TicketApprovalEntity, Long> {
    List<TicketApprovalEntity> findByTicketId(Long ticketId);
}
