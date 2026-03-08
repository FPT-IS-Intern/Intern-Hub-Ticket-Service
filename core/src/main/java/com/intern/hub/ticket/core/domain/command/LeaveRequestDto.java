package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record LeaveRequestDto(
        Long ticketId,
        Long leaveTypeId,
        Integer totalDays,
        com.intern.hub.ticket.core.domain.model.TicketStatus status) {
}
