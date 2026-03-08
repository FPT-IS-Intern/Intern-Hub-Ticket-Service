package com.intern.hub.ticket.core.port.in;

import java.util.List;

import com.intern.hub.ticket.core.domain.command.TicketDto;

public interface GetUserTicketsUseCase {
    List<TicketDto> getUserTickets(Long userId);
}
