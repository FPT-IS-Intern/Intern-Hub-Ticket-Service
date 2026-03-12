package com.intern.hub.ticket.core.domain.model.command;

public record ApproveTicketCommand(
        Long ticketId,
        Long approverId,
        String comment,
        String idempotencyKey) {
}