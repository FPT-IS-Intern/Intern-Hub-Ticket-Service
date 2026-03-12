package com.intern.hub.ticket.core.domain.usecase;

import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;

public interface CreateTicketUsecase {
    TicketModel create(CreateTicketCommand command);
}
