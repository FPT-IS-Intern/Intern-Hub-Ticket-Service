package com.intern.hub.ticket.core.domain.port;

import java.util.List;
import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.TicketGlobalApproverModel;

public interface TicketGlobalApproverRepository {
    Optional<TicketGlobalApproverModel> findByApproverId(Long approverId);
    List<Long> findApproverIdsByMinLevel(int minLevel);
    TicketGlobalApproverModel save(TicketGlobalApproverModel model);
    void deleteByApproverId(Long approverId);
}

