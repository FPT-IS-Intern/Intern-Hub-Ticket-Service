package com.intern.hub.ticket.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_types", schema = "ih_ticket")
public class TicketTypeEntity extends AuditEntity {

    @Id
    @Column(name = "ticket_type_id")
    private Long ticketTypeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "description")
    private String description;
}
