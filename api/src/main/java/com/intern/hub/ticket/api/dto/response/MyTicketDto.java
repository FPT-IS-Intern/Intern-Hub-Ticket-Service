package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public record MyTicketDto(
                @JsonSerialize(using = ToStringSerializer.class) Long ticketId,
                String typeName,
                String senderFullName,
                Long createdAt,
                String reason,
                String approverFullNameLevel1,
                String approverFullNameLevel2,
                String statusLevel1,
                String statusLevel2,
                TicketStatus status
        ) {
}
