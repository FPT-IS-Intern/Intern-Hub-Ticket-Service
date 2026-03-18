package com.intern.hub.ticket.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalRule {
    private String condition;
    private Integer levelsIfTrue;
    private Integer levelsIfFalse;
}
