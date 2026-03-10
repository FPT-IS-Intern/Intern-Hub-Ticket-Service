package com.intern.hub.ticket.core.usecase;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.CreateLeaveRequestCommand;
import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.domain.model.LeaveRequest;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.port.in.LeaveRequestUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.LeaveRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class LeaveRequestService implements LeaveRequestUseCase {

        private final TicketRepository ticketRepository;
        private final LeaveRequestRepository leaveRequestRepository;
        private final TicketTypeRepository ticketTypeRepository;
        private final IdGenerator idGenerator;

        @Override
        public TicketDto createLeaveRequest(CreateLeaveRequestCommand command) {
                TicketType ticketType = ticketTypeRepository.findById(command.leaveTypeId())
                                .orElseThrow(() -> new NotFoundException("Ticket Type not found"));

                Long ticketId = idGenerator.nextId();

                OffsetDateTime now = OffsetDateTime.now();

                if (command.startAt() == null || command.endAt() == null) {
                        throw new BadRequestException("startAt/endAt required");
                }

                if (command.startAt().isBefore(now)) {
                        throw new BadRequestException("startAt cannot be in the past");
                }

                if (command.reason() == null || command.reason().isBlank()) {
                        throw new BadRequestException("reason cannot be empty");
                }

                OffsetDateTime start = command.startAt();
                OffsetDateTime end = command.endAt();

                if (!start.isBefore(end) && !start.toLocalDate().equals(end.toLocalDate()))
                        throw new BadRequestException("startAt must be before endAt");

                long daysLong = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate()) + 1L;

                if (daysLong <= 0)
                        throw new BadRequestException("Invalid leave duration");
                if (daysLong > 1000)
                        throw new BadRequestException("Requested leave too long");

                int totalDays = Math.toIntExact(daysLong);

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

                LeaveRequest leaveRequest = LeaveRequest.builder()
                                .ticketId(ticketId)
                                .leaveTypeId(command.leaveTypeId())
                                .totalDays(totalDays)
                                .build();
                leaveRequestRepository.save(leaveRequest);

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
