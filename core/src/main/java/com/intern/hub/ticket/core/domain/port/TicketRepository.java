//Version 2
package com.intern.hub.ticket.core.domain.port;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public interface TicketRepository {
    TicketModel save(TicketModel model);

    Optional<TicketModel> findById(Long id);

    List<TicketModel> findAll();

    List<TicketModel> findByStatus(TicketStatus status);
}