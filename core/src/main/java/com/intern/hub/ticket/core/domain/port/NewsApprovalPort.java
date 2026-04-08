package com.intern.hub.ticket.core.domain.port;

public interface NewsApprovalPort {
    void notifyNewsApproved(Long ticketId);
    void notifyNewsRejected(Long ticketId);
}
