package com.intern.hub.ticket.core.domain.model;

import java.time.OffsetDateTime;

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
    private OffsetDateTime actionAt;
    private TicketApprovalStatus status;
    private Integer version;
}
