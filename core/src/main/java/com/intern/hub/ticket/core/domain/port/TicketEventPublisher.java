package com.intern.hub.ticket.core.domain.port;

public interface TicketEventPublisher {
    void publishTicketApprovedEvent(Long eventId, Long ticketId, Long approverId);

    void publishTicketCreatedEvent(Long eventId, Long ticketId, Long userId, Long ticketTypeId);
}
