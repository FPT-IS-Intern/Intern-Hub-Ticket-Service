package com.intern.hub.ticket.core.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TicketType extends BaseAuditDomain {
    private Long ticketTypeId;
    private String typeName;
    private String description;
}
