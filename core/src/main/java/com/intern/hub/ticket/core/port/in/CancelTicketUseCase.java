package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.CancelTicketCommand;

public interface CancelTicketUseCase {
    void cancelTicket(CancelTicketCommand command);
}
