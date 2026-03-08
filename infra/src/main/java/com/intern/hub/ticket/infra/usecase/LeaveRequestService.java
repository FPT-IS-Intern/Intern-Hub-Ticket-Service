package com.intern.hub.ticket.infra.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.CreateLeaveRequestCommand;
import com.intern.hub.ticket.core.domain.command.TicketDto;
import com.intern.hub.ticket.core.domain.model.LeaveRequest;
import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.core.domain.model.TicketStatus;
import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.core.port.in.LeaveRequestUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.LeaveRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LeaveRequestService implements LeaveRequestUseCase {

        private final TicketRepository ticketRepository;
        private final LeaveRequestRepository leaveRequestRepository;
        private final TicketTypeRepository ticketTypeRepository;
        private final IdGenerator idGenerator;

        @Override
        public TicketDto createLeaveRequest(CreateLeaveRequestCommand command) {
                TicketType ticketType = ticketTypeRepository.findById(command.getLeaveTypeId())
                                .orElseThrow(() -> new NotFoundException("Ticket Type not found"));

                Long ticketId = idGenerator.nextId();

                Ticket ticket = Ticket.builder()
                                .ticketId(ticketId)
                                .userId(command.getUserId())
                                .ticketTypeId(ticketType.getTicketTypeId())
                                .startAt(command.getStartAt())
                                .endAt(command.getEndAt())
                                .reason(command.getReason())
                                .status(TicketStatus.PENDING)
                                .build();
                ticketRepository.save(ticket);

                LeaveRequest leaveRequest = LeaveRequest.builder()
                                .ticketId(ticketId)
                                .leaveTypeId(command.getLeaveTypeId())
                                .totalDays(command.getTotalDays())
                                .status(TicketStatus.PENDING)
                                .version(1)
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
                                .build();
        }
}
