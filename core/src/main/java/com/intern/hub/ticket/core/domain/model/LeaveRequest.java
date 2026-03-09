package com.intern.hub.ticket.core.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LeaveRequest extends BaseAuditDomain {
    private Long ticketId;
    private Long leaveTypeId;
    private Integer totalDays;
    private Integer version;
}
