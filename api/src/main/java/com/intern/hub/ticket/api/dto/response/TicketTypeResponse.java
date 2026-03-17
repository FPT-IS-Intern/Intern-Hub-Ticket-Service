package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TicketTypeResponse (
    @JsonSerialize(using = ToStringSerializer.class) Long ticketTypeId,
    String typeName
){}
