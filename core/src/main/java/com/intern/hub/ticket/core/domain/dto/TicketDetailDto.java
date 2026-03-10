package com.intern.hub.ticket.core.domain.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.Builder;

@Builder
public record TicketDetailDto(
        Long ticketId,
        Long userId,
        Long ticketTypeId,
        String ticketTypeName,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String reason,
        TicketStatus status,
        LeaveRequestDto leaveRequest,
        RemoteRequestDto remoteRequest,
        List<EvidenceDto> evidences,
        List<TicketApprovalDto> approvals) {
}
