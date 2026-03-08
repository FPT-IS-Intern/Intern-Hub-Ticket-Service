package com.intern.hub.ticket.infra.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.EvidenceDto;
import com.intern.hub.ticket.core.domain.command.LeaveRequestDto;
import com.intern.hub.ticket.core.domain.command.RemoteRequestDto;
import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;
import com.intern.hub.ticket.core.domain.command.TicketDetailDto;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.GetTicketDetailUseCase;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.core.port.repository.LeaveRequestRepository;
import com.intern.hub.ticket.core.port.repository.RemoteRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetTicketDetailService implements GetTicketDetailUseCase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final RemoteRequestRepository remoteRequestRepository;
    private final EvidenceRepository evidenceRepository;
    private final TicketApprovalRepository ticketApprovalRepository;

    @Override
    public TicketDetailDto getTicketDetail(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        String ticketTypeName = ticketTypeRepository.findById(ticket.getTicketTypeId())
                .map(TicketType::getTypeName)
                .orElse("Unknown Type");

        // Build base detail
        TicketDetailDto.TicketDetailDtoBuilder builder = TicketDetailDto.builder()
                .ticketId(ticket.getTicketId())
                .userId(ticket.getUserId())
                .ticketTypeId(ticket.getTicketTypeId())
                .ticketTypeName(ticketTypeName)
                .startAt(ticket.getStartAt())
                .endAt(ticket.getEndAt())
                .reason(ticket.getReason())
                .status(ticket.getStatus());

        // Load type-specific details
        leaveRequestRepository.findByTicketId(ticketId)
                .ifPresent(lr -> builder.leaveRequest(LeaveRequestDto.builder()
                        .ticketId(lr.getTicketId())
                        .leaveTypeId(lr.getLeaveTypeId())
                        .totalDays(lr.getTotalDays())
                        .status(lr.getStatus())
                        .build()));

        remoteRequestRepository.findByTicketId(ticketId)
                .ifPresent(rr -> builder.remoteRequest(RemoteRequestDto.builder()
                        .ticketId(rr.getTicketId())
                        .workLocationId(rr.getWorkLocationId())
                        .startTime(rr.getStartTime())
                        .endTime(rr.getEndTime())
                        .remoteType(rr.getRemoteType())
                        .build()));

        // Load evidences
        List<EvidenceDto> evidences = evidenceRepository.findByTicketId(ticketId).stream()
                .map(e -> EvidenceDto.builder()
                        .evidenceId(e.getEvidenceId())
                        .ticketId(e.getTicketId())
                        .evidenceFolder(e.getEvidenceFolder())
                        .evidenceUrl(e.getEvidenceUrl())
                        .uploadedAt(e.getUploadedAt())
                        .fileType(e.getFileType())
                        .fileSize(e.getFileSize())
                        .status(e.getStatus())
                        .build())
                .collect(Collectors.toList());
        builder.evidences(evidences);

        // Load approval history
        List<TicketApprovalDto> approvals = ticketApprovalRepository.findByTicketId(ticketId).stream()
                .map(a -> TicketApprovalDto.builder()
                        .approvalId(a.getApprovalId())
                        .ticketId(a.getTicketId())
                        .approverId(a.getApproverId())
                        .action(a.getAction())
                        .comment(a.getComment())
                        .actionAt(a.getActionAt())
                        .status(a.getStatus())
                        .build())
                .collect(Collectors.toList());
        builder.approvals(approvals);

        return builder.build();
    }
}
