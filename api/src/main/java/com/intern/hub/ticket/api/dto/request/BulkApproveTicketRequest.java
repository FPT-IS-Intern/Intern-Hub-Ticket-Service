package com.intern.hub.ticket.api.dto.request;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.command.TicketApproveItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record BulkApproveTicketRequest(

                @NotBlank(message = "The Idempotency Key cannot be left blank.") String idempotencyKey,

                @NotEmpty(message = "The ticket list must not be blank.") List<TicketApproveItem> tickets,

                String comment) {

}