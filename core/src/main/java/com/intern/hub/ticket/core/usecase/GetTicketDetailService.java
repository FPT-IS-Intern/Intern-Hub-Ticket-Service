package com.intern.hub.ticket.core.usecase;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.dto.TicketDetailDto;
import com.intern.hub.ticket.core.port.in.GetTicketDetailUseCase;
import com.intern.hub.ticket.core.port.repository.TicketDetailQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Retrieves full ticket detail by leveraging a dedicated aggregate query
 * repository.
 * This resolves the N+1 query issue by orchestrating optimized database fetch
 * strategies.
 */
@RequiredArgsConstructor
public class GetTicketDetailService implements GetTicketDetailUseCase {

        private final TicketDetailQueryRepository ticketDetailQueryRepository;

        @Override
        public TicketDetailDto getTicketDetail(Long ticketId) {
                return ticketDetailQueryRepository.findTicketDetailById(ticketId)
                                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        }
}
