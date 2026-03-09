package com.intern.hub.ticket.core.usecase;

import java.util.List;

import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.port.in.GetAllTicketsUseCase;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllTicketsService implements GetAllTicketsUseCase {

    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> TicketDto.builder()
                        .ticketId(ticket.getTicketId())
                        .userId(ticket.getUserId())
                        .ticketTypeId(ticket.getTicketTypeId())
                        .ticketTypeName(ticket.getTicketTypeName())
                        .startAt(ticket.getStartAt())
                        .endAt(ticket.getEndAt())
                        .reason(ticket.getReason())
                        .status(ticket.getStatus())
                        .build())
                .toList();
    }
}
