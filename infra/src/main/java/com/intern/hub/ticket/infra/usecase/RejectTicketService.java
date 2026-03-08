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
import com.intern.hub.ticket.core.port.in.RejectTicketUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RejectTicketService implements RejectTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final IdGenerator idGenerator;

    @Override
    public TicketApprovalDto rejectTicket(ReviewTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.getTicketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!"PENDING".equals(ticket.getStatus())) {
            throw new BadRequestException("Only PENDING tickets can be rejected");
        }

        // Update ticket status
        ticket.setStatus("REJECTED");
        ticketRepository.save(ticket);

        // Record approval history
        TicketApproval approval = TicketApproval.builder()
                .approvalId(idGenerator.nextId())
                .ticketId(ticket.getTicketId())
                .approverId(command.getApproverId())
                .action("REJECTED")
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
