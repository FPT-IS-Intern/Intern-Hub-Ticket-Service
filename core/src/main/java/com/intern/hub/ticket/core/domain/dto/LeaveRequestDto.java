package com.intern.hub.ticket.core.domain.dto;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.Builder;

@Builder
public record LeaveRequestDto(
                Long ticketId,
                Long leaveTypeId,
                Integer totalDays,
                TicketStatus status) {
}
