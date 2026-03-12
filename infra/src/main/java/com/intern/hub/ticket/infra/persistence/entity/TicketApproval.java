package com.intern.hub.ticket.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ticket_approvals")
public class TicketApproval extends AuditEntity {
    @Id
    @Column(name = "approval_id")
    Long id;

    @Column(nullable = false)
    Long ticketId;

    Long approverId;

    @Column(length = 50)
    String action;

    @Column(columnDefinition = "text")
    String comment;

    @Column(unique = true)
    String idempotencyKey;

    Long actionAt;

    @Column(length = 50)
    String status;

    @Version
    Integer version;
}
