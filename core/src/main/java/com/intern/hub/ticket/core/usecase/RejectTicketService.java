package com.intern.hub.ticket.core.usecase;

import java.time.OffsetDateTime;

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
import com.intern.hub.ticket.core.port.in.RejectTicketUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class RejectTicketService implements RejectTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final IdGenerator idGenerator;

    @Override
    public TicketApprovalDto rejectTicket(ReviewTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (TicketStatus.PENDING != ticket.getStatus()) {
            throw new BadRequestException("Only PENDING tickets can be rejected");
        }

        // Update ticket status
        ticket.setStatus(TicketStatus.REJECTED);
        ticket = ticketRepository.save(ticket);

        // Record approval history
        TicketApproval approval = TicketApproval.builder()
                .approvalId(idGenerator.nextId())
                .ticketId(ticket.getTicketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.REJECTED)
                .comment(command.comment())
                .actionAt(OffsetDateTime.now())
                .status(TicketApprovalStatus.APPROVED)
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
