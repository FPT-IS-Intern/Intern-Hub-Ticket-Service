package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ApproverPermissionResponse(
        @JsonSerialize(using = ToStringSerializer.class) Long approverId,
        Integer maxApprovalLevel,
        boolean canApproveLevel1,
        boolean canApproveLevel2
) {
}
