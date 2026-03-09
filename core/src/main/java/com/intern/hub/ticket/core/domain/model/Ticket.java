package com.intern.hub.ticket.core.domain.model;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Ticket extends BaseAuditDomain {
    private Long ticketId;
    private Long userId;
    private Long ticketTypeId;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private String reason;
    private TicketStatus status;
    private String ticketTypeName;
    private Integer version;
    private boolean isDeleted;
}
