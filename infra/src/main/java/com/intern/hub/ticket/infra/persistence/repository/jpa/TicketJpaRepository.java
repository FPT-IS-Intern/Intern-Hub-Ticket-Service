package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;

@Repository
public interface TicketJpaRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByUserId(Long userId);

    @Modifying
    @Query("""
    update Ticket t
    set t.status = :status,
        t.updatedBy = :updatedBy,
        t.updatedAt = :updatedAt,
        t.version = t.version + 1
    where t.ticketId = :ticketId
      and t.version = :version
      and t.status not in ('APPROVED', 'REJECTED')
""")
    int rejectTicket(
            @Param("ticketId") Long ticketId,
            @Param("status") TicketStatus status,
            @Param("updatedBy") Long updatedBy,
            @Param("updatedAt") Long updatedAt,
            @Param("version") Integer version
    );
}
