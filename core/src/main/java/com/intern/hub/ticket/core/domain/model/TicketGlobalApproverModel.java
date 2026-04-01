package com.intern.hub.ticket.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketGlobalApproverModel {
    Long approverId;
    Integer maxApprovalLevel;
}

