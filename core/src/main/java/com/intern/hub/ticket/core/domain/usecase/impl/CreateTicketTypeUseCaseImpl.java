package com.intern.hub.ticket.core.domain.usecase.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketTypeUseCase;
import com.intern.hub.library.common.exception.ConflictDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTicketTypeUseCaseImpl implements CreateTicketTypeUseCase {

    private final TicketTypeRepository ticketTypePort;

    @Override
    @Transactional
    public TicketTypeModel create(CreateTicketTypeCommand command) {
        
        if (ticketTypePort.existsByNameAndIsDeletedFalse(command.typeName())) {
            throw new ConflictDataException("conflict.resource.exists","Name already exists");
        }

        TicketTypeModel newType = TicketTypeModel.builder()
                .typeName(command.typeName())
                .description(command.description())
                .isDeleted(false) 
                .build();

        return ticketTypePort.save(newType);
    }
}