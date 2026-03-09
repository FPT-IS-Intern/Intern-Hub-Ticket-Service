package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.dto.TicketDetailDto;

public interface GetTicketDetailUseCase {
    TicketDetailDto getTicketDetail(Long ticketId);
}
