package com.intern.hub.ticket.core.domain.usecase;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;

public interface CreateTicketTypeUseCase {
    TicketTypeModel create(CreateTicketTypeCommand command);
}