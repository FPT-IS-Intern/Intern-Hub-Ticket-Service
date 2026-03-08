package com.intern.hub.ticket.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "ticket_types")
public class TicketTypeEntity extends BaseAuditEntity {

    @Id
    @Column(name = "ticket_type_id")
    private Long ticketTypeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "description")
    private String description;
}
