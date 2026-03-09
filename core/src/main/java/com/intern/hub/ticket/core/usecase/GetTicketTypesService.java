package com.intern.hub.ticket.core.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.intern.hub.ticket.core.domain.dto.TicketTypeDto;
import com.intern.hub.ticket.core.port.in.GetTicketTypesUseCase;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetTicketTypesService implements GetTicketTypesUseCase {

    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public List<TicketTypeDto> getTicketTypes() {
        return ticketTypeRepository.findAll().stream()
                .map(type -> TicketTypeDto.builder()
                        .id(type.getTicketTypeId())
                        .name(type.getTypeName())
                        .description(type.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
