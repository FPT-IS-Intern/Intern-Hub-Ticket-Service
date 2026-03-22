package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

public interface ManageTicketTypeApproverUseCase {
    void assignApprover(Long ticketTypeId, Long approverId, Long adminId);
    void removeApprover(Long ticketTypeId, Long approverId);
    List<Long> getApproverIds(Long ticketTypeId);
}
