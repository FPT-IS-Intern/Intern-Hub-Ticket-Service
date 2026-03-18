package com.intern.hub.ticket.core.domain.model.command;

public record RejectTicketCommand(
    Long ticketId,
    Long approverId,
    String comment,
    String idempotencyKey,
    Integer version
) {}
