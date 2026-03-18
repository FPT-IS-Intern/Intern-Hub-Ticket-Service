package com.intern.hub.ticket.core.domain.usecase;

public interface ManageTicketTypeApproverUseCase {
    void assignApprover(Long ticketTypeId, Long approverId, Long adminId);
    void removeApprover(Long ticketTypeId, Long approverId);
}