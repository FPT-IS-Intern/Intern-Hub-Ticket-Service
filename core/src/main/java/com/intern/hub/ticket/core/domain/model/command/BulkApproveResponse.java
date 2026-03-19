package com.intern.hub.ticket.core.domain.model.command;

import java.util.List;

public record BulkApproveResponse(
                int totalProcessed,
                int successCount,
                List<TicketErrorDetail> failedTickets) {
        public record TicketErrorDetail(Long ticketId, String errorMessage) {
        }
}
