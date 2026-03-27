package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public record TicketDto(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

        String fullName,
        String email,

        @JsonSerialize(using = ToStringSerializer.class) Long ticketTypeId,

        TicketStatus status,
        Long createdAt,
        Long updatedAt,
        Long createdBy,
        Long updatedBy,
        String approverFullName
        ) {
}

