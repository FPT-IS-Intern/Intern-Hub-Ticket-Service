package com.intern.hub.ticket.app.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.command.CreateLeaveRequestCommand;
import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.port.in.LeaveRequestUseCase;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class TransactionalLeaveRequestApplicationService implements LeaveRequestUseCase {

    private final LeaveRequestUseCase leaveRequestUseCase;

    @Override
    @Transactional
    public TicketDto createLeaveRequest(CreateLeaveRequestCommand command) {
        return leaveRequestUseCase.createLeaveRequest(command);
    }
}
