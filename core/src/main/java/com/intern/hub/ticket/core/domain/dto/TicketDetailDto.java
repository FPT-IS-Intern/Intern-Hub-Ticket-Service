package com.intern.hub.ticket.core.domain.dto;

import java.time.LocalDate;
import java.util.List;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import lombok.Builder;

@Builder
public record TicketDetailDto(
        Long ticketId,
        Long userId,
        Long ticketTypeId,
        String ticketTypeName,
        LocalDate startAt,
        LocalDate endAt,
        String reason,
        TicketStatus status,
        LeaveRequestDto leaveRequest,
        RemoteRequestDto remoteRequest,
        List<EvidenceDto> evidences,
        List<TicketApprovalDto> approvals) {
}
