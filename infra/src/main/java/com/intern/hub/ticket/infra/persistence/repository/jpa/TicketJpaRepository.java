package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import com.intern.hub.ticket.infra.model.ressponse.ApprovalDetailInfoProjection;
import org.springframework.data.domain.Pageable;
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


    @Query("""
        SELECT t FROM Ticket t
        JOIN t.ticketType tt
        WHERE tt.typeName IN :typeNames
        ORDER BY t.createdAt DESC
    """)
    List<Ticket> findTopByUserIdAndTypeNameInOrderByCreatedAtDesc(
            @Param("typeNames") List<String> typeNames,
            Pageable pageable
    );

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
    WHERE NOT EXISTS (
        SELECT 1
        FROM tickets t
        JOIN ticket_types tt ON t.ticket_type_id = tt.ticket_type_id
        WHERE t.user_id = u.user_id
          AND tt.type_name = 'Phiếu nghỉ phép'
          AND (
              (t.payload->>'start_date')::date <= CURRENT_DATE
              AND (t.payload->>'end_date')::date >= CURRENT_DATE
          )
    )
""", nativeQuery = true)
    int countEmployeesWorkingToday(@Param("userIds") Long[] userIds);


    @Query(value = """
                SELECT COUNT(DISTINCT t.user_id)
                FROM tickets t
                JOIN ticket_types tt ON t.ticket_type_id = tt.ticket_type_id
                WHERE tt.type_name = 'Phiếu Remote - WFH'
                AND (
                    (t.payload->>'start_date')::date <= CURRENT_DATE
                    AND (t.payload->>'end_date')::date >= CURRENT_DATE
                );
            """, nativeQuery = true)
    int totalPeopleWorkInHome();

    @Query(value = """
                SELECT COUNT(DISTINCT t.user_id)
                FROM tickets t
                JOIN ticket_types tt ON t.ticket_type_id = tt.ticket_type_id
                WHERE tt.type_name = 'Phiếu Remote - Onsite'
                AND (t.payload->>'location') = 'Onsite'
                AND (
                    (t.payload->>'start_date')::date <= CURRENT_DATE
                    AND (t.payload->>'end_date')::date >= CURRENT_DATE
                );
            """, nativeQuery = true)
    int totalWorkingOnsite();

    @Query(value = """
            SELECT 
                t.ticket_id AS ticketId,
                t.user_id AS userId,
                t.created_at AS createdAt,
            
                l1.approver_id AS approverIdLevel1,
                l1.created_at AS approvedAt,
                l1.status AS statusLevel1,
            
                l2.approver_id AS approverIdLevel2,
                l2.created_at AS approvedAtLevel2,
                l2.status AS statusLevel2
            
            FROM tickets t
            
            LEFT JOIN LATERAL (
                SELECT * FROM ticket_approvals
                WHERE ticket_id = t.ticket_id
                  AND approval_level = 1
                ORDER BY created_at DESC
                LIMIT 1
            ) l1 ON true
            
            LEFT JOIN LATERAL (
                SELECT * FROM ticket_approvals
                WHERE ticket_id = t.ticket_id
                  AND approval_level = 2
                ORDER BY created_at DESC
                LIMIT 1
            ) l2 ON true
            
            WHERE t.ticket_id = :ticketId
            """, nativeQuery = true)
    ApprovalDetailInfoProjection getApprovalDetailInfo(@Param("ticketId") Long ticketId);
}
