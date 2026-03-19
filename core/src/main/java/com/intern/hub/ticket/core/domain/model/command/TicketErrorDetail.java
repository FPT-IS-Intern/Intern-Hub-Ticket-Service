package com.intern.hub.ticket.core.domain.model.command;

public record TicketErrorDetail(
                Long ticketId,
                String errorMessage) {
}
