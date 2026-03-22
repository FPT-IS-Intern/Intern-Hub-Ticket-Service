package com.intern.hub.ticket.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ApproverIdsResponse(
        @JsonSerialize(contentUsing = ToStringSerializer.class) List<Long> approverIds
) {
}
