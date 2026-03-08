package com.intern.hub.ticket.core.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.intern.hub.ticket.core.domain.command.TicketDto;
import com.intern.hub.ticket.core.domain.model.TicketStatus;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.GetPendingTicketsUseCase;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPendingTicketsService implements GetPendingTicketsUseCase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public List<TicketDto> getPendingTickets() {
        return ticketRepository.findByStatus(TicketStatus.PENDING).stream()
                .map(ticket -> {
                    String ticketTypeName = ticketTypeRepository.findById(ticket.getTicketTypeId())
                            .map(TicketType::getTypeName)
                            .orElse("Unknown Type");

                    return TicketDto.builder()
                            .ticketId(ticket.getTicketId())
                            .userId(ticket.getUserId())
                            .ticketTypeId(ticket.getTicketTypeId())
                            .ticketTypeName(ticketTypeName)
                            .startAt(ticket.getStartAt())
                            .endAt(ticket.getEndAt())
                            .reason(ticket.getReason())
                            .status(ticket.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
