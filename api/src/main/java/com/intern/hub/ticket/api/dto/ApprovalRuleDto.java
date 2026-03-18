package com.intern.hub.ticket.api.dto;

import lombok.Builder;

@Builder
public record ApprovalRuleDto(
        String condition,
        int levelsIfTrue,
        int levelsIfFalse
) {
}
