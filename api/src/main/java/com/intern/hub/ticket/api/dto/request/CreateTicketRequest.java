package com.intern.hub.ticket.api.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(
        @NotNull(message = "ticketTypeId is required") Long ticketTypeId,

        @NotNull(message = "payload is required") Map<String, Object> payload) {
}