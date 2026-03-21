package com.intern.hub.ticket.infra.persistence.entity;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "outbox_events")
@EntityListeners(AuditingEntityListener.class)
public class OutboxEvent extends AuditEntity {

    @Id
    @Column(name = "event_id")
    Long eventId;

    String aggregateType;
    String aggregateId;
    String eventType;

    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Enumerated(EnumType.STRING)
    OutboxStatus status;

}