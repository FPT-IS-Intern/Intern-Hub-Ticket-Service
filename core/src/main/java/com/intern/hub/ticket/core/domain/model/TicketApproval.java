package com.intern.hub.ticket.core.domain.model;

import java.time.OffsetDateTime;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketApproval extends AuditEntity {
    private Long approvalId;
    private Long ticketId;
    private Long approverId;
    private TicketApprovalAction action;
    private String comment;
    private OffsetDateTime actionAt;
    private TicketApprovalStatus status;
    private Integer version;
}
