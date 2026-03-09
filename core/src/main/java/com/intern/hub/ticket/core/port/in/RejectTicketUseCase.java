package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.dto.TicketApprovalDto;

public interface RejectTicketUseCase {
    TicketApprovalDto rejectTicket(ReviewTicketCommand command);
}
