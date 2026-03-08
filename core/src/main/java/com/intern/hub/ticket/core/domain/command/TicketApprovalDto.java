package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import com.intern.hub.ticket.core.domain.model.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.TicketApprovalStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketApprovalDto {
    private Long approvalId;
    private Long ticketId;
    private Long approverId;
    private TicketApprovalAction action;
    private String comment;
    private LocalDate actionAt;
    private TicketApprovalStatus status;
}
