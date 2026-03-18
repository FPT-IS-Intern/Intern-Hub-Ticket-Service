package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.HashMap;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTicketUsecaseImpl implements CreateTicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketEventPublisher ticketEventPublisher;
    private final Snowflake snowflake;

    @Override
    public TicketModel create(CreateTicketCommand command) {

        TicketModel ticket = TicketModel.builder()
                .ticketId(snowflake.next())
                .userId(command.userId())
                .ticketTypeId(command.ticketTypeId())
                .status(TicketStatus.PENDING)
                .payload(command.payload() != null ? command.payload() : new HashMap<>())
                .isDeleted(false)
                .build();

        TicketModel savedTicket = ticketRepository.save(ticket);

        ticketEventPublisher.publishTicketCreatedEvent(
                snowflake.next(),
                savedTicket.getTicketId(),
                savedTicket.getUserId(),
                savedTicket.getTicketTypeId());

        return savedTicket;
    }
}