package com.intern.hub.ticket.infra.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.command.TicketDto;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.GetAllTicketsUseCase;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAllTicketsService implements GetAllTicketsUseCase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
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
