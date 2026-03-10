package com.intern.hub.ticket.core.usecase;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.CreateRemoteRequestCommand;
import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.domain.model.RemoteRequest;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.port.in.RemoteRequestUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.RemoteRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
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

                if (command.startAt() == null || command.endAt() == null) {
                        throw new BadRequestException("startAt and endAt are required");
                }

                OffsetDateTime start = command.startAt();
                OffsetDateTime end = command.endAt();

                if (!start.isBefore(end)) {
                        throw new BadRequestException("startAt must be before endAt");
                }

                OffsetDateTime now = OffsetDateTime.now();
                if (start.isBefore(now)) {
                        throw new BadRequestException("startAt cannot be in the past");
                }

                if (command.reason() == null || command.reason().isBlank()) {
                        throw new BadRequestException("reason cannot be empty");
                }

                if (command.workLocationId() == null) {
                        throw new BadRequestException("workLocationId is required");
                }

                if (command.remoteType() == null) {
                        throw new BadRequestException("remoteType required");
                }

                long daysInclusive = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate()) + 1;
                if (daysInclusive > 365) {
                        throw new BadRequestException("requested period too long");
                }
                int totalDays = Math.toIntExact(daysInclusive);

                Ticket ticket = Ticket.builder()
                                .ticketId(ticketId)
                                .userId(command.userId())
                                .ticketTypeId(ticketType.getTicketTypeId())
                                .startAt(command.startAt())
                                .endAt(command.endAt())
                                .reason(command.reason())
                                .status(TicketStatus.PENDING)
                                .build();
                ticket = ticketRepository.save(ticket);

                RemoteRequest remoteRequest = RemoteRequest.builder()
                                .ticketId(ticketId)
                                .workLocationId(command.workLocationId())
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
                                .createdAt(TicketDto.toOffsetDateTime(ticket.getCreatedAt()))
                                .updatedAt(TicketDto.toOffsetDateTime(ticket.getUpdatedAt()))
                                .createdBy(ticket.getCreatedBy())
                                .updatedBy(ticket.getUpdatedBy())
                                .build();
        }
}
