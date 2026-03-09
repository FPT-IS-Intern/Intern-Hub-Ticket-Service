package com.intern.hub.ticket.core.usecase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.GetAllTicketsUseCase;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllTicketsService implements GetAllTicketsUseCase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public List<TicketDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        List<Long> typeIds = tickets.stream()
                .map(Ticket::getTicketTypeId)
                .distinct()
                .toList();

        Map<Long, String> typeNames = ticketTypeRepository.findAllById(typeIds).stream()
                .collect(Collectors.toMap(TicketType::getTicketTypeId, TicketType::getTypeName));
        return tickets.stream().map(ticket -> TicketDto.builder()
                .ticketId(ticket.getTicketId())
                .userId(ticket.getUserId())
                .ticketTypeId(ticket.getTicketTypeId())
                .ticketTypeName(typeNames.getOrDefault(ticket.getTicketTypeId(), "Unknown Type"))
                .startAt(ticket.getStartAt())
                .endAt(ticket.getEndAt())
                .reason(ticket.getReason())
                .status(ticket.getStatus())
                .build())
                .toList();
    }
}
