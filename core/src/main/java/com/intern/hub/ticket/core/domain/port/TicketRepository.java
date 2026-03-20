//Version 2
package com.intern.hub.ticket.core.domain.port;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

public interface TicketRepository {
    TicketModel save(TicketModel model);

    Optional<TicketModel> findById(Long id);

    List<TicketModel> findAll();

    List<TicketModel> findByStatus(TicketStatus status);

    PaginatedData<TicketModel> findAllPaginated(int page, int size);

    Collection<TicketModel> findByUserId(Long userId);
}