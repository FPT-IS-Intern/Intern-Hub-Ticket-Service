package com.intern.hub.ticket.infra.adapter.outbound.worker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;
import com.intern.hub.ticket.infra.persistence.entity.OutboxEvent;
import com.intern.hub.ticket.infra.persistence.repository.jpa.OutboxEventJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayWorker {

    private final OutboxEventJpaRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TICKET_TOPIC = "ih.ticket.events";

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (pendingEvents.isEmpty()) {
            return;
        }

        // Tạo 2 list để gom ID lại
        List<Long> successIds = new ArrayList<>();
        List<Long> failedIds = new ArrayList<>();

        for (OutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send(TICKET_TOPIC, event.getAggregateId(), event.getPayload()).get();
                successIds.add(event.getEventId());

                // TUYỆT ĐỐI KHÔNG GỌI: event.setStatus("PUBLISHED") Ở ĐÂY NỮA

            } catch (Exception e) {
                log.error("Failed to send outbox event to Kafka. EventID: {}", event.getEventId(), e);
                failedIds.add(event.getEventId());

                // TUYỆT ĐỐI KHÔNG GỌI: event.setStatus("FAILED") Ở ĐÂY NỮA
            }
        }

        long currentTime = System.currentTimeMillis();

        // Cập nhật một lốc bằng câu query, bypass hoàn toàn Auditing
        if (!successIds.isEmpty()) {
            outboxRepository.updateStatus(OutboxStatus.PUBLISHED, currentTime, successIds);
        }

        if (!failedIds.isEmpty()) {
            outboxRepository.updateStatus(OutboxStatus.FAILED, currentTime, failedIds);
        }
    }
}