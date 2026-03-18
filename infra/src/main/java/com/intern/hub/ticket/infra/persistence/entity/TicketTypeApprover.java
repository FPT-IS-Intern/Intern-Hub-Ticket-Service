package com.intern.hub.ticket.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;
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
@Table(name = "ticket_type_approvers")
public class TicketTypeApprover extends AuditEntity{

    @Id
    @Column(name = "id")
    Long ticketTypeApproverId;

    @Column(nullable = false)
    Long ticketTypeId;

    @Column(nullable = false)
    Long approverId;
}
