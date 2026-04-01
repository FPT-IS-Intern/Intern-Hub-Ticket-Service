package com.intern.hub.ticket.core.domain.port;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;

public interface TicketTypeApproverRepository {
    boolean exists(Long ticketTypeId, Long approverId, Integer approvalLevel);
    TicketTypeApproverModel save(TicketTypeApproverModel model);
    void delete(Long ticketTypeId, Long approverId, Integer approvalLevel);
    List<Long> findApproverIdsByTicketTypeId(Long ticketTypeId);
    List<Long> findApproverIdsByTicketTypeIdAndApprovalLevel(Long ticketTypeId, Integer approvalLevel);
    List<Integer> findApprovalLevels(Long ticketTypeId, Long approverId);
}
