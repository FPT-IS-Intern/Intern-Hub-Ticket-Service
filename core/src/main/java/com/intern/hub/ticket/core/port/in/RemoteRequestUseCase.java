package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.CreateRemoteRequestCommand;
import com.intern.hub.ticket.core.domain.command.TicketDto;

public interface RemoteRequestUseCase {
    TicketDto createRemoteRequest(CreateRemoteRequestCommand command);
}
