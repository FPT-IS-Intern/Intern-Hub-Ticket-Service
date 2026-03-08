package com.intern.hub.ticket.core.usecase;

import java.time.LocalDate;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketApproval;
import com.intern.hub.ticket.core.domain.model.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.TicketApprovalStatus;
import com.intern.hub.ticket.core.domain.model.TicketStatus;
import com.intern.hub.ticket.core.port.in.ApproveTicketUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveTicketService implements ApproveTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final IdGenerator idGenerator;

    @Override
    public TicketApprovalDto approveTicket(ReviewTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (TicketStatus.PENDING != ticket.getStatus()) {
            throw new BadRequestException("Only PENDING tickets can be approved");
        }

        // Update ticket status
        ticket.setStatus(TicketStatus.APPROVED);
        ticketRepository.save(ticket);

        // Record approval history
        TicketApproval approval = TicketApproval.builder()
                .approvalId(idGenerator.nextId())
                .ticketId(ticket.getTicketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.APPROVED)
                .comment(command.comment())
                .actionAt(LocalDate.now())
                .status(TicketApprovalStatus.COMPLETED)
                .version(1)
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
