package com.intern.hub.ticket.core.usecase;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.dto.TicketApprovalDto;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketApproval;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.port.in.ApproveTicketUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class ApproveTicketService implements ApproveTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final IdGenerator idGenerator;

    @Override
    public TicketApprovalDto approveTicket(ReviewTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (TicketStatus.PENDING != ticket.getStatus() && TicketStatus.IN_REVIEW != ticket.getStatus()) {
            throw new BadRequestException("Only PENDING or IN_REVIEW tickets can be approved");
        }

        OffsetDateTime created = Instant.ofEpochMilli(ticket.getCreatedAt())
                .atOffset(ZoneOffset.UTC);
        OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);
        boolean olderThanFiveDays = nowUtc.isAfter(created.plusMinutes(5));

        TicketApprovalAction action;
        TicketApprovalStatus approvalStatus;

        if (ticket.getStatus() == TicketStatus.IN_REVIEW) {
            action = TicketApprovalAction.APPROVED;
            approvalStatus = TicketApprovalStatus.APPROVED;
            ticket.setStatus(TicketStatus.APPROVED);

        } else {
            if (olderThanFiveDays) {
                action = TicketApprovalAction.IN_APPROVING;
                approvalStatus = TicketApprovalStatus.IN_REVIEW;

                ticket.setStatus(TicketStatus.IN_REVIEW);
            } else {
                action = TicketApprovalAction.APPROVED;
                approvalStatus = TicketApprovalStatus.APPROVED;
                ticket.setStatus(TicketStatus.APPROVED);
            }
        }

        ticket = ticketRepository.save(ticket);

        TicketApproval approval = TicketApproval.builder()
                .approvalId(idGenerator.nextId())
                .ticketId(ticket.getTicketId())
                .approverId(command.approverId())
                .action(action)
                .comment(command.comment())
                .actionAt(nowUtc)
                .status(approvalStatus)
                .build();

        ticketApprovalRepository.save(approval);

        return TicketApprovalDto.builder()
                .approvalId(approval.getApprovalId())
                .ticketId(approval.getTicketId())
                .approverId(approval.getApproverId())
                .action(approval.getAction())
                .comment(approval.getComment())
                .actionAt(approval.getActionAt())
                .status(approval.getStatus())
                .build();
    }
}
