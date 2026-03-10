package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record ReviewTicketCommand(
                Long ticketId,
                Long approverId,
                String comment) {
}
