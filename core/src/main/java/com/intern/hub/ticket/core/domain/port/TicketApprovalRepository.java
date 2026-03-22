package com.intern.hub.ticket.core.domain.port;

import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;

public interface TicketApprovalRepository {
    TicketApprovalModel save(TicketApprovalModel model);

    boolean existsByIdempotencyKey(String idempotencyKey);

    Optional<TicketApprovalModel> findByTicketId(Long ticketId);
}
