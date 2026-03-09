package com.intern.hub.ticket.infra.persistence.entity;

import java.time.OffsetDateTime;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "tickets", schema = "ih_ticket")
public class TicketEntity extends BaseAuditEntity {

    @Id
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ticket_type_id", nullable = false, insertable = false, updatable = false)
    private Long ticketTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketTypeEntity ticketType;

    @Column(name = "start_at")
    private OffsetDateTime startAt;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TicketStatus status;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
