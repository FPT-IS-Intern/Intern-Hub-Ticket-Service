package com.intern.hub.ticket.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TicketTypeDetailDTO(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketTypeId,

        String typeName,

        String description,

        Object formConfig,

        ApprovalRuleDto approvalRule,

        boolean requireEvidence,

        List<ApproverDto> approvers) {

    public record ApprovalRuleDto(
            String condition,
            Integer levelsIfTrue,
            Integer levelsIfFalse) {
    }

    public record ApproverDto(
            @JsonSerialize(using = ToStringSerializer.class) Long approverId,
            String approverName) {
    }
}
