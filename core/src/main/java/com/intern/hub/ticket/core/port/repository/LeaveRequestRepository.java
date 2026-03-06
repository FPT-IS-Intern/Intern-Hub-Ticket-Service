package com.intern.hub.ticket.core.port.repository;

import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.LeaveRequest;

public interface LeaveRequestRepository {
    Optional<LeaveRequest> findByTicketId(Long ticketId);

    LeaveRequest save(LeaveRequest leaveRequest);

    void deleteByTicketId(Long ticketId);
}
