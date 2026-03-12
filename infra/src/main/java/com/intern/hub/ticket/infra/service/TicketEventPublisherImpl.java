package com.intern.hub.ticket.infra.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketEventPublisherImpl implements TicketEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_TICKET_APPROVED = "ticket.ticket.approved";

    @Override
    public void publishTicketApprovedEvent(Long eventId, Long ticketId, Long approverId) {
        TicketApprovedEventPayload payload = new TicketApprovedEventPayload(eventId, ticketId, approverId);
        kafkaTemplate.send(TOPIC_TICKET_APPROVED, payload);
    }

    private record TicketApprovedEventPayload(
            Long eventId,
            Long ticketId,
            Long approverId) {
    }
}
