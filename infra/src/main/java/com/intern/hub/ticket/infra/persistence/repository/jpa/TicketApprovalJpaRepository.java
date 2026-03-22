package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.infra.persistence.entity.TicketApproval;

@Repository
public interface TicketApprovalJpaRepository extends JpaRepository<TicketApproval, Long> {

    boolean existsByIdempotencyKey(String idempotencyKey);

    Optional<TicketApproval> findByTicketId(Long ticketId);
}