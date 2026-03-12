package com.intern.hub.ticket.core.domain.model;

import com.intern.hub.starter.security.entity.AuditModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;

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
public class TicketApprovalModel extends AuditModel {

    Long approvalId;

    Long ticketId;

    Long approverId;

    TicketApprovalAction action;

    String comment;

    String idempotencyKey;

    Long actionAt;

    TicketApprovalStatus status;

    @Setter(AccessLevel.NONE)
    Integer version;
}
