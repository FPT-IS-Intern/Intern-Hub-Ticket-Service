package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewTicketCommand {
    private Long ticketId;
    private Long approverId;
    private String comment;
}
