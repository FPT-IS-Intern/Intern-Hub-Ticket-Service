package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApproveTicketRequest(
        String comment,
        @NotBlank(message = "Idempotency key is required") String idempotencyKey,
        @NotNull(message = "Version is required")Integer version) {
}