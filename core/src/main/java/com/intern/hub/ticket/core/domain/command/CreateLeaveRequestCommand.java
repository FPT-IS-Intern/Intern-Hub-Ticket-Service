package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateLeaveRequestCommand {
    private Long userId;
    private Long leaveTypeId;
    private LocalDate startAt;
    private LocalDate endAt;
    private String reason;
    private Integer totalDays;
}
