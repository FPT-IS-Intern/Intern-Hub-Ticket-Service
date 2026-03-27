package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import com.intern.hub.ticket.infra.model.ressponse.ApprovalDetailInfoProjection;
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

    @Query("select count(t) from Ticket t")
    int totalTicket();

    @Query("select count(t) from Ticket t where t.status = 'PENDING'")
    int totalPendingTicket();

    @Query("select count(t) from Ticket t where t.status = 'APPROVED'")
    int totalApprovedTicket();

    @Query("select count(t) from Ticket t where t.status = 'REJECTED'")
    int totalRejectedTicket();

    @Query(value = """
    SELECT COUNT(*)
    FROM unnest(:userIds) AS u(user_id)
    WHERE u.user_id NOT IN (
        SELECT t.user_id
        FROM tickets t
        JOIN ticket_types tt ON t.ticket_type_id = tt.ticket_type_id
        WHERE t.created_at >= (EXTRACT(EPOCH FROM CURRENT_DATE) * 1000)
          AND t.created_at < (EXTRACT(EPOCH FROM CURRENT_DATE + INTERVAL '1 day') * 1000)
          AND tt.type_name IN ('Phiếu nghỉ phép', 'Phiếu Remote - WFH')
    )
""", nativeQuery = true)
    int countEmployeesWorkingToday(@Param("userIds") List<Long> userIds);


    @Query(value = """
                SELECT COUNT(DISTINCT t.user_id)
                FROM tickets t
                JOIN ticket_types tt ON t.ticket_type_id = tt.ticket_type_id
                WHERE t.created_at >= (EXTRACT(EPOCH FROM CURRENT_DATE) * 1000)
                  AND t.created_at < (EXTRACT(EPOCH FROM CURRENT_DATE + INTERVAL '1 day') * 1000)
                  AND tt.type_name = 'Phiếu Remote - WFH'
            """, nativeQuery = true)
    int totalPeopleWorkInHome();

    @Query(value = """
            SELECT 
                t.ticket_id AS ticketId,
                t.user_id AS userId,
                t.created_at AS createdAt,
            
                ta_first.approver_id AS approverIdLevel1,
                ta_first.created_at AS approvedAt,
                ta_first.status AS statusLevel1,
            
                ta_last.approver_id AS approverIdLevel2,
                ta_last.created_at AS approvedAtLevel2,
                ta_last.status AS statusLevel2
            
            FROM tickets t
            
            LEFT JOIN LATERAL (
                SELECT * FROM ticket_approvals 
                WHERE ticket_id = t.ticket_id 
                ORDER BY created_at ASC 
                LIMIT 1
            ) ta_first ON true
            
            LEFT JOIN LATERAL (
                SELECT * FROM ticket_approvals 
                WHERE ticket_id = t.ticket_id 
                ORDER BY created_at DESC 
                LIMIT 1
            ) ta_last ON true
            
            WHERE t.ticket_id = :ticketId
            """, nativeQuery = true)
    ApprovalDetailInfoProjection getApprovalDetailInfo(@Param("ticketId") Long ticketId);
}
