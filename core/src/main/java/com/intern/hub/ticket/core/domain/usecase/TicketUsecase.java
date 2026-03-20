package com.intern.hub.ticket.core.domain.usecase;

import java.util.Collection;
import java.util.List;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;

public interface TicketUsecase {
    TicketModel create(CreateTicketCommand command);

    TicketModel getTicketDetail(Long ticketId);

    List<TicketModel> getPendingTickets();

    PaginatedData<TicketModel> getAllTickets(int page, int size);

    Collection<TicketModel> getMyTickets(Long userId);
}
