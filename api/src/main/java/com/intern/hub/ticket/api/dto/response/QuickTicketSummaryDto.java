package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public record QuickTicketSummaryDto(
                @JsonSerialize(using = ToStringSerializer.class) Long ticketId,
                Long createdAt,
                String fullName,
                TicketStatus status
        ) {
}
