package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TicketApprovalInfoDto(
                @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

                String senderFullName,

                Long createdAt,

                String approverFullNameLevel1,
                Long approvedAt,
                String statusLevel1,

                String approverFullNameLevel2,
                Long approvedAtLevel2,
                String statusLevel2
        ) {
}
