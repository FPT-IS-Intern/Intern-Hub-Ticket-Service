package com.intern.hub.ticket.core.domain.model.command;

import java.util.List;

public record BulkApproveTicketCommand(
                String idempotencyKey,
                List<TicketApproveItem> tickets,
                Long approverId,
                String comment) {
}
