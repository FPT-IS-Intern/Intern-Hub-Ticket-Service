package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TicketTypeDto(
        @JsonSerialize(using = ToStringSerializer.class) 
        Long ticketTypeId,
        
        String typeName,
        String description
) {}