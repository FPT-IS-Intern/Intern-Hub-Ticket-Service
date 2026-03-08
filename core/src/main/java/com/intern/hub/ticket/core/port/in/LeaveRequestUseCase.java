package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.CreateLeaveRequestCommand;
import com.intern.hub.ticket.core.domain.command.TicketDto;

public interface LeaveRequestUseCase {
    TicketDto createLeaveRequest(CreateLeaveRequestCommand command);
}
