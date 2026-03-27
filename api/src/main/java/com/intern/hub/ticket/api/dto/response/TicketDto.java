package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public record TicketDto(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

        @JsonSerialize(using = ToStringSerializer.class) Long userId,

        @JsonSerialize(using = ToStringSerializer.class) Long ticketTypeId,

        TicketStatus status,
        Long createdAt,
        Long updatedAt,
        Long createdBy,
        Long updatedBy,
        @JsonSerialize(using = ToStringSerializer.class) Long approverId
        ) {
}
