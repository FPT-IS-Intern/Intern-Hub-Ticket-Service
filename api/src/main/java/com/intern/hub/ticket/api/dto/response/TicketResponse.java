package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public record TicketResponse(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

        TicketStatus status) {
}
