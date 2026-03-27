package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.infra.persistence.entity.TicketApproval;

@Repository
public interface TicketApprovalJpaRepository extends JpaRepository<TicketApproval, Long> {

    boolean existsByIdempotencyKey(String idempotencyKey);

    Optional<TicketApproval> findByTicketId(Long ticketId);

    @Query(value = """
            SELECT ta.ticket_id AS ticketId, ta.approver_id AS approverId
            FROM ticket_approvals ta
            INNER JOIN (
                SELECT ticket_id, MAX(created_at) AS max_created_at
                FROM ticket_approvals
                WHERE ticket_id IN :ticketIds
                GROUP BY ticket_id
            ) latest ON ta.ticket_id = latest.ticket_id AND ta.created_at = latest.max_created_at
            """, nativeQuery = true)
    List<Object[]> findLatestApproverIdsByTicketIds(@Param("ticketIds") List<Long> ticketIds);
}