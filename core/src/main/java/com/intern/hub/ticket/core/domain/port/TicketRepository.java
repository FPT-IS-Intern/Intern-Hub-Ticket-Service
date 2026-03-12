//Version 2
package com.intern.hub.ticket.core.domain.port;

import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketModel;

public interface TicketRepository {
    TicketModel save(TicketModel model);

    Optional<TicketModel> findById(Long id);
}