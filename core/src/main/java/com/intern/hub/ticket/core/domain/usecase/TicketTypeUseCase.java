package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.model.command.UpdateTicketTypeCommand;

public interface TicketTypeUseCase {
    TicketTypeModel create(CreateTicketTypeCommand command);

    TicketTypeModel update(UpdateTicketTypeCommand command);

    void delete(Long ticketTypeId);

    TicketTypeModel getById(Long ticketTypeId);

    List<TicketTypeModel> getAll();
}