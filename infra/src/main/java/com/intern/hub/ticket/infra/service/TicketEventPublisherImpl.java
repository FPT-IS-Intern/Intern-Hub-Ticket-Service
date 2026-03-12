package com.intern.hub.ticket.infra.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.infra.persistence.entity.OutboxEvent;
import com.intern.hub.ticket.infra.persistence.repository.jpa.OutboxEventJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketEventPublisherImpl implements TicketEventPublisher {

        private final OutboxEventJpaRepository outboxRepo;

        @Override
        public void publishTicketApprovedEvent(Long eventId, Long ticketId, Long approverId) {
                Map<String, Object> payload = Map.of(
                                "eventId", eventId,
                                "ticketId", ticketId,
                                "approverId", approverId);

                OutboxEvent outbox = OutboxEvent.builder()
                                .id(eventId)
                                .aggregateType("TICKET")
                                .aggregateId(ticketId.toString())
                                .eventType("TICKET_APPROVED")
                                .payload(payload)
                                .status(OutboxStatus.PENDING)
                                .build();

                outboxRepo.save(outbox);
        }

        @Override
        public void publishTicketCreatedEvent(Long eventId, Long ticketId, Long userId, Long ticketTypeId) {
                Map<String, Object> payload = Map.of(
                                "eventId", eventId,
                                "ticketId", ticketId,
                                "userId", userId,
                                "ticketTypeId", ticketTypeId);

                OutboxEvent outbox = OutboxEvent.builder()
                                .id(eventId)
                                .aggregateType("TICKET")
                                .aggregateId(ticketId.toString())
                                .eventType("TICKET_CREATED")
                                .payload(payload)
                                .status(OutboxStatus.PENDING)
                                .build();

                outboxRepo.save(outbox);
        }
}