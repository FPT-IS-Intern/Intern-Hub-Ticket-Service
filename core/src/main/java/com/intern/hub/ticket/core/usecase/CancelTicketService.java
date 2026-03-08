package com.intern.hub.ticket.core.usecase;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.ForbiddenException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.CancelTicketCommand;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.port.in.CancelTicketUseCase;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelTicketService implements CancelTicketUseCase {

    private final TicketRepository ticketRepository;

    @Override
    public void cancelTicket(CancelTicketCommand command) {
        Ticket ticket = ticketRepository.findById(command.getTicketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!ticket.getUserId().equals(command.getRequesterId())) {
            throw new ForbiddenException("Only the ticket creator can cancel this ticket");
        }

        if ("APPROVED".equals(ticket.getStatus()) || "REJECTED".equals(ticket.getStatus())
                || "CANCELLED".equals(ticket.getStatus())) {
            throw new BadRequestException(
                    "Ticket cannot be cancelled from its current status: " + ticket.getStatus());
        }

        ticket.setStatus("CANCELLED");
        ticketRepository.save(ticket);
    }
}
