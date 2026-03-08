package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record CancelTicketCommand(
        Long ticketId,
        Long requesterId) {
}
