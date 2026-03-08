package com.intern.hub.ticket.core.port.in;

import java.util.List;

import com.intern.hub.ticket.core.domain.command.TicketTypeDto;

public interface GetTicketTypesUseCase {
    List<TicketTypeDto> getTicketTypes();
}
