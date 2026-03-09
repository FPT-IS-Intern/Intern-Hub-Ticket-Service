package com.intern.hub.ticket.core.port.repository;

import java.util.Optional;

import com.intern.hub.ticket.core.domain.dto.TicketDetailDto;

public interface TicketDetailQueryRepository {
    Optional<TicketDetailDto> findTicketDetailById(Long ticketId);
}
