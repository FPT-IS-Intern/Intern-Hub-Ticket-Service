package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveRequestDto {
    private Long ticketId;
    private Long leaveTypeId;
    private Integer totalDays;
    private String status;
}
