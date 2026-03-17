package com.intern.hub.ticket.core.domain.model.command;

public record CreateTicketTypeCommand(
    String typeName,
    String description
) {}
