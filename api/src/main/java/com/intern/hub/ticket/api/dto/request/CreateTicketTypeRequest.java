package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateTicketTypeRequest(
    @NotNull(message = "ticketTypeId is required") String typeName,
    String description
) {} 