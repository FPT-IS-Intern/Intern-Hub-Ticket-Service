package com.intern.hub.ticket.core.domain.port;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;

public interface TicketTypeRepository {
    boolean existsByNameAndIsDeletedFalse(String name);

    TicketTypeModel save(TicketTypeModel model);

    Optional<TicketTypeModel> findById(Long id);

    List<TicketTypeModel> findAll();

    List<TicketTypeModel> findAllActive();

    List<TicketTypeModel> findAllByIds(List<Long> ids);
}