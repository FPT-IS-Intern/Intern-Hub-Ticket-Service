package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.ReviewTicketCommand;
import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;

public interface ApproveTicketUseCase {
    TicketApprovalDto approveTicket(ReviewTicketCommand command);
}
