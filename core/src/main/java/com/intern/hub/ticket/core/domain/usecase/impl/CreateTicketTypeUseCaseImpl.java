package com.intern.hub.ticket.core.domain.usecase.impl;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketTypeUseCase;
import com.intern.hub.library.common.exception.ConflictDataException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTicketTypeUseCaseImpl implements CreateTicketTypeUseCase {

    private final TicketTypeRepository ticketTypePort;

    @Override
    public TicketTypeModel create(CreateTicketTypeCommand command) {
        
        if (ticketTypePort.existsByNameAndIsDeletedFalse(command.typeName())) {
            throw new ConflictDataException("conflict.data","Name already exists");
        }

        TicketTypeModel newType = TicketTypeModel.builder()
                .typeName(command.typeName())
                .description(command.description())
                .template(command.template())
                .isDeleted(false) 
                .build();

        return ticketTypePort.save(newType);
    }
}