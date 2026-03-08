package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

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
    private String action;
    private String comment;
    private LocalDate actionAt;
    private String status;
}
