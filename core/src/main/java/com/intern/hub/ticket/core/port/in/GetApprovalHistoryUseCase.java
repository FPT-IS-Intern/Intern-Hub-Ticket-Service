package com.intern.hub.ticket.core.port.in;

import java.util.List;

import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;

public interface GetApprovalHistoryUseCase {
    List<TicketApprovalDto> getApprovalHistory(Long ticketId);
}
