package com.intern.hub.ticket.core.domain.model;

import java.time.LocalDate;

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
    private LocalDate startAt;
    private LocalDate endAt;
    private String reason;
    private String status;
}
