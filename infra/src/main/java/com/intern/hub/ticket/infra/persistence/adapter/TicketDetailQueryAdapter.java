package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.dto.EvidenceDto;
import com.intern.hub.ticket.core.domain.dto.LeaveRequestDto;
import com.intern.hub.ticket.core.domain.dto.RemoteRequestDto;
import com.intern.hub.ticket.core.domain.dto.TicketApprovalDto;
import com.intern.hub.ticket.core.domain.dto.TicketDetailDto;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.core.port.repository.TicketDetailQueryRepository;
import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;
import com.intern.hub.ticket.infra.persistence.repository.JpaLeaveRequestRepository;
import com.intern.hub.ticket.infra.persistence.repository.JpaRemoteRequestRepository;
import com.intern.hub.ticket.infra.persistence.repository.JpaTicketApprovalRepository;
import com.intern.hub.ticket.infra.persistence.repository.JpaTicketDetailRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketDetailQueryAdapter implements TicketDetailQueryRepository {

        private final JpaTicketDetailRepository jpaTicketDetailRepository;
        private final JpaLeaveRequestRepository leaveRequestRepository;
        private final JpaRemoteRequestRepository remoteRequestRepository;
        private final EvidenceRepository evidenceRepository;
        private final JpaTicketApprovalRepository ticketApprovalRepository;

        @Override
        @Transactional(readOnly = true)
        public Optional<TicketDetailDto> findTicketDetailById(Long ticketId) {
                // Fetch Ticket with TicketType (1 query with JOIN)
                Optional<TicketEntity> ticketOpt = jpaTicketDetailRepository.findTicketWithRelationById(ticketId);

                if (ticketOpt.isEmpty()) {
                        return Optional.empty();
                }

                TicketEntity ticket = ticketOpt.get();
                String ticketTypeName = ticket.getTicketType() != null ? ticket.getTicketType().getTypeName()
                                : "Unknown Type";

                TicketDetailDto.TicketDetailDtoBuilder builder = TicketDetailDto.builder()
                                .ticketId(ticket.getTicketId())
                                .userId(ticket.getUserId())
                                .ticketTypeId(ticket.getTicketTypeId())
                                .ticketTypeName(ticketTypeName)
                                .startAt(ticket.getStartAt())
                                .endAt(ticket.getEndAt())
                                .reason(ticket.getReason())
                                .status(ticket.getStatus());

                // Fetch sub-types based on existence (Max 1 query)
                leaveRequestRepository.findById(ticketId).ifPresent(lr -> builder.leaveRequest(LeaveRequestDto.builder()
                                .ticketId(lr.getTicketId())
                                .leaveTypeId(lr.getLeaveTypeId())
                                .totalDays(lr.getTotalDays())
                                .status(ticket.getStatus())
                                .build()));

                remoteRequestRepository.findById(ticketId)
                                .ifPresent(rr -> builder.remoteRequest(RemoteRequestDto.builder()
                                                .ticketId(rr.getTicketId())
                                                .workLocationId(rr.getWorkLocationId())
                                                .startTime(ticket.getStartAt())
                                                .endTime(ticket.getEndAt())
                                                .remoteType(rr.getRemoteType())
                                                .build()));

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

                return Optional.of(builder.build());
        }
}
