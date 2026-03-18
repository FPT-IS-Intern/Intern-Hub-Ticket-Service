package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;

public interface GetTicketUsecase {
    TicketModel getTicketDetail(Long ticketId);

    List<TicketModel> getPendingTickets();

    PaginatedData<TicketModel> getAllTickets(int page, int size);
}
