package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

public interface ManageTicketTypeApproverUseCase {
    void assignApprover(Long ticketTypeId, Long approverId, Integer approvalLevel, Long adminId);
    void removeApprover(Long ticketTypeId, Long approverId, Integer approvalLevel);
    List<Long> getApproverIds(Long ticketTypeId, Integer approvalLevel);
}
