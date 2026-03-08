package com.intern.hub.ticket.infra.usecase;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketApproval;
import com.intern.hub.ticket.core.port.in.ApproveTicketUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApproveTicketService implements ApproveTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final IdGenerator idGenerator;

    @Override
    public TicketApprovalDto approveTicket(ReviewTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.getTicketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!"PENDING".equals(ticket.getStatus())) {
            throw new BadRequestException("Only PENDING tickets can be approved");
        }

        // Update ticket status
        ticket.setStatus("APPROVED");
        ticketRepository.save(ticket);

        // Record approval history
        TicketApproval approval = TicketApproval.builder()
                .approvalId(idGenerator.nextId())
                .ticketId(ticket.getTicketId())
                .approverId(command.getApproverId())
                .action("APPROVED")
                .comment(command.getComment())
                .actionAt(LocalDate.now())
                .status("COMPLETED")
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
