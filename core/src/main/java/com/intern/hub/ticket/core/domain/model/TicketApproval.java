package com.intern.hub.ticket.core.domain.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TicketApproval extends BaseAuditDomain {
    private Long approvalId;
    private Long ticketId;
    private Long approverId;
    private TicketApprovalAction action;
    private String comment;
    private LocalDate actionAt;
    private TicketApprovalStatus status;
    private Integer version;
}
