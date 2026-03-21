package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.TicketModel;

public interface TicketEventPublisher {
    void publishTicketApprovedEvent(Long eventId, Long ticketId, Long approverId);

    void publishTicketCreatedEvent(TicketModel ticket);
}
