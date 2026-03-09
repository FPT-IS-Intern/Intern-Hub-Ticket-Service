package com.intern.hub.ticket.core.usecase;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.CreateRemoteRequestCommand;
import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.domain.model.RemoteRequest;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketStatus;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.RemoteRequestUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.RemoteRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoteRequestService implements RemoteRequestUseCase {

        private final TicketRepository ticketRepository;
        private final RemoteRequestRepository remoteRequestRepository;
        private final TicketTypeRepository ticketTypeRepository;
        private final IdGenerator idGenerator;

        @Override
        public TicketDto createRemoteRequest(CreateRemoteRequestCommand command) {

                TicketType ticketType = ticketTypeRepository.findByTypeName("Remote Work") // Or fetch dynamically if
                                                                                           // you
                                                                                           // pass type
                                                                                           // id in command
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Ticket Type 'Remote Work' not found"));

                Long ticketId = idGenerator.nextId();

                Ticket ticket = Ticket.builder()
                                .ticketId(ticketId)
                                .userId(command.userId())
                                .ticketTypeId(ticketType.getTicketTypeId())
                                .startAt(command.startAt())
                                .endAt(command.endAt())
                                .reason(command.reason())
                                .status(TicketStatus.PENDING)
                                .version(1)
                                .build();
                ticketRepository.save(ticket);

                RemoteRequest remoteRequest = RemoteRequest.builder()
                                .ticketId(ticketId)
                                .workLocationId(command.workLocationId())
                                .startTime(command.startAt())
                                .endTime(command.endAt())
                                .remoteType(command.remoteType())
                                .build();
                remoteRequestRepository.save(remoteRequest);

                return TicketDto.builder()
                                .ticketId(ticket.getTicketId())
                                .userId(ticket.getUserId())
                                .ticketTypeId(ticket.getTicketTypeId())
                                .ticketTypeName(ticketType.getTypeName())
                                .startAt(ticket.getStartAt())
                                .endAt(ticket.getEndAt())
                                .reason(ticket.getReason())
                                .status(ticket.getStatus())
                                .build();
        }
}
