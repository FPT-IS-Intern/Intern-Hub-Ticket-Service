package com.intern.hub.ticket.infra.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.hub.ticket.infra.persistence.entity.TicketTypeApprover;

public interface TicketTypeApproverJpaRepository extends JpaRepository<TicketTypeApprover, Long> {
    
    boolean existsByTicketTypeIdAndApproverId(Long ticketTypeId, Long approverId);

    // Hàm này để Admin tước quyền
    void deleteByTicketTypeIdAndApproverId(Long ticketTypeId, Long approverId);
}