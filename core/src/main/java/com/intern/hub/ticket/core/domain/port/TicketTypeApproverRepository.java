package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;

public interface TicketTypeApproverRepository {
    boolean exists(Long ticketTypeId, Long approverId);
    TicketTypeApproverModel save(TicketTypeApproverModel model);
    void delete(Long ticketTypeId, Long approverId);
}
