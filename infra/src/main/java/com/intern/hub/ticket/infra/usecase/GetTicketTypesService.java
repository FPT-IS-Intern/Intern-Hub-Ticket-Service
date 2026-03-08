package com.intern.hub.ticket.infra.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.command.TicketTypeDto;
import com.intern.hub.ticket.core.port.in.GetTicketTypesUseCase;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
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
