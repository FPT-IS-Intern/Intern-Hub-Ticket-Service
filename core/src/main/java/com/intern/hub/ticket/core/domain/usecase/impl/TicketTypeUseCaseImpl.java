package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.model.command.UpdateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.TicketTypeUseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketTypeUseCaseImpl implements TicketTypeUseCase {

    private final TicketTypeRepository ticketTypePort;

    @Override
    @Transactional
    public TicketTypeModel create(CreateTicketTypeCommand command) {

        if (ticketTypePort.existsByNameAndIsDeletedFalse(command.typeName())) {
            throw new ConflictDataException("conflict.data", "Name already exists");
        }

        TicketTypeModel newType = TicketTypeModel.builder()
                .typeName(command.typeName())
                .description(command.description())
                .template(command.template())
                .approvalRule(command.approvalRule())
                .isDeleted(false)
                .build();

        return ticketTypePort.save(newType);
    }

    @Override
    public TicketTypeModel update(UpdateTicketTypeCommand command) {
        TicketTypeModel existingType = ticketTypePort.findById(command.ticketTypeId())
                .orElseThrow(() -> new NotFoundException("Ticket type not found"));

        if (ticketTypePort.existsByNameAndIsDeletedFalse(command.typeName())) {
            throw new ConflictDataException("conflict.data", "Name already exists");
        }

        if (command.template() != null) {
            existingType.setTemplate(command.template());
        }

        if (command.approvalRule() != null) {
            existingType.setApprovalRule(command.approvalRule());
        }

        if (command.description() != null) {
            existingType.setDescription(command.description());
        }
        if (command.typeName() != null) {
            existingType.setTypeName(command.typeName());
        }

        return ticketTypePort.save(existingType);
    }

    @Override
    public void delete(Long ticketTypeId) {
        TicketTypeModel existingType = ticketTypePort.findById(ticketTypeId)
                .orElseThrow(() -> new NotFoundException("Ticket type not found"));
        existingType.setIsDeleted(true);
        ticketTypePort.save(existingType);
    }

    @Override
    public TicketTypeModel getById(Long ticketTypeId) {
        return ticketTypePort.findById(ticketTypeId)
                .orElseThrow(() -> new NotFoundException("Ticket type not found"));
    }

    @Override
    public List<TicketTypeModel> getAll() {
        return ticketTypePort.findAll();
    }
}