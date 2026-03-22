package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intern.hub.ticket.infra.persistence.entity.TicketTypeApprover;

public interface TicketTypeApproverJpaRepository extends JpaRepository<TicketTypeApprover, Long> {
    
    boolean existsByTicketTypeIdAndApproverId(Long ticketTypeId, Long approverId);

    // Hàm này để Admin tước quyền
    void deleteByTicketTypeIdAndApproverId(Long ticketTypeId, Long approverId);

    @Query("select t.approverId from TicketTypeApprover t where t.ticketTypeId = :ticketTypeId")
    List<Long> findApproverIdsByTicketTypeId(@Param("ticketTypeId") Long ticketTypeId);
}
