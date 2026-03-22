package com.intern.hub.ticket.infra.adapter.outbound;

import org.springframework.stereotype.Component;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.infra.persistence.entity.OutboxEvent;
import com.intern.hub.ticket.infra.persistence.repository.jpa.OutboxEventJpaRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OutboxTicketEventPublisherAdapter implements TicketEventPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxEventJpaRepository outboxRepository;
    private final Snowflake snowflake;

    @Override
    public void publishTicketCreatedEvent(TicketModel ticket) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(ticket);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventId(snowflake.next())
                    .aggregateType("TICKET")
                    .aggregateId(String.valueOf(ticket.getTicketId()))
                    .eventType("TICKET_CREATED")
                    .payload(jsonPayload)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxRepository.save(outboxEvent);

        } catch (Exception e) {
            throw new RuntimeException("Cannot process outbox event creation", e);
        }
    }

    @Override
    public void publishTicketApprovedEvent(Long eventId, Long ticketId, Long approverId) {
        try {
            Map<String, Object> payload = Map.of(
                    "ticketId", ticketId,
                    "approverId", approverId,
                    "eventId", eventId);
            String jsonPayload = objectMapper.writeValueAsString(payload);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventId(eventId)
                    .aggregateType("TICKET")
                    .aggregateId(String.valueOf(ticketId))
                    .eventType("TICKET_APPROVED")
                    .payload(jsonPayload)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxRepository.save(outboxEvent);

        } catch (Exception e) {
            throw new RuntimeException("Cannot process outbox event creation for ticket approval", e);
        }
    }

}
