package com.intern.hub.ticket.core.domain.model.command;

public record TicketApproveItem(
        Long ticketId,
        Integer version) {
}
