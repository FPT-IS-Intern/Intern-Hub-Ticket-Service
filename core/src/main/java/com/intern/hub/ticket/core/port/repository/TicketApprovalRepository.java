package com.intern.hub.ticket.core.port.repository;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketApproval;

public interface TicketApprovalRepository {
    Optional<TicketApproval> findById(Long approvalId);

    List<TicketApproval> findByTicketId(Long ticketId);

    TicketApproval save(TicketApproval ticketApproval);

    void deleteById(Long approvalId);
}
