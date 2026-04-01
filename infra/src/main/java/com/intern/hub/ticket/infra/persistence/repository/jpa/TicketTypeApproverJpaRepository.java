package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intern.hub.ticket.infra.persistence.entity.TicketTypeApprover;

public interface TicketTypeApproverJpaRepository extends JpaRepository<TicketTypeApprover, Long> {
    
    boolean existsByTicketTypeIdAndApproverIdAndApprovalLevel(Long ticketTypeId, Long approverId, Integer approvalLevel);

    // Hàm này để Admin tước quyền
    void deleteByTicketTypeIdAndApproverId(Long ticketTypeId, Long approverId);

    void deleteByTicketTypeIdAndApproverIdAndApprovalLevel(Long ticketTypeId, Long approverId, Integer approvalLevel);

    @Query("select t.approverId from TicketTypeApprover t where t.ticketTypeId = :ticketTypeId")
    List<Long> findApproverIdsByTicketTypeId(@Param("ticketTypeId") Long ticketTypeId);

    @Query("select t.approverId from TicketTypeApprover t where t.ticketTypeId = :ticketTypeId and t.approvalLevel = :approvalLevel")
    List<Long> findApproverIdsByTicketTypeIdAndApprovalLevel(
            @Param("ticketTypeId") Long ticketTypeId,
            @Param("approvalLevel") Integer approvalLevel);

    @Query("select t.approvalLevel from TicketTypeApprover t where t.ticketTypeId = :ticketTypeId and t.approverId = :approverId")
    List<Integer> findApprovalLevels(
            @Param("ticketTypeId") Long ticketTypeId,
            @Param("approverId") Long approverId);
}
