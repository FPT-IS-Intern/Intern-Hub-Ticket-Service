package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;
import java.util.List;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDetailDto {
    // Base ticket info
    private Long ticketId;
    private Long userId;
    private Long ticketTypeId;
    private String ticketTypeName;
    private LocalDate startAt;
    private LocalDate endAt;
    private String reason;
    private TicketStatus status;

    // Type-specific details (only one will be non-null)
    private LeaveRequestDto leaveRequest;
    private RemoteRequestDto remoteRequest;

    // Related data
    private List<EvidenceDto> evidences;
    private List<TicketApprovalDto> approvals;
}
