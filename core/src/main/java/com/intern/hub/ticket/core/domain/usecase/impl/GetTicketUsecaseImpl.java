package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.usecase.GetTicketUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetTicketUsecaseImpl implements GetTicketUsecase {

    private final TicketRepository ticketRepository;

    @Override
    public TicketModel getTicketDetail(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found with id: " + ticketId));
    }

    @Override
    public List<TicketModel> getPendingTickets() {
        return ticketRepository.findByStatus(TicketStatus.PENDING);
    }

    @Override
    public PaginatedData<TicketModel> getAllTickets(int page, int size) {
        return ticketRepository.findAllPaginated(page, size);
    }

}
