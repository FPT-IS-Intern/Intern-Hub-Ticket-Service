package com.intern.hub.ticket.infra.worker;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;
import com.intern.hub.ticket.infra.persistence.entity.OutboxEvent;
import com.intern.hub.ticket.infra.persistence.repository.jpa.OutboxEventJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxRelayWorker {

    private final OutboxEventJpaRepository outboxRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelayString = "5000")
    @Transactional
    public void relayEvents() {
        List<OutboxEvent> pendingEvents = outboxRepo.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents) {
            try {
                String topic = "ticket.ticket." + event.getEventType().split("_")[1].toLowerCase();

                kafkaTemplate.send(topic, event.getPayload());

                event.setStatus(OutboxStatus.PUBLISHED);
                outboxRepo.save(event);

            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                outboxRepo.save(event);
            }
        }
    }
}
