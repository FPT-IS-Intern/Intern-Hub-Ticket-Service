package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ApproveTicketRequest(
        String comment,
        @NotBlank(message = "Idempotency key is required") String idempotencyKey) {
}