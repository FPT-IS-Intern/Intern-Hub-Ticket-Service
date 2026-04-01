package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

public interface ManageTicketGlobalApproverUseCase {
    /**
     * @param level 1 or 2. Level 1 returns all approvers. Level 2 returns approvers with maxApprovalLevel=2.
     */
    List<Long> getApproverIds(Integer level);

    /**
     * Ensure approver has at least the given level (1 or 2). If level=2, it upgrades to level 2.
     */
    void assignApprover(Long approverId, Integer level, Long actorId);

    /**
     * Remove or downgrade:
     * level=1 -> remove approval right entirely.
     * level=2 -> downgrade from 2 to 1 if exists.
     */
    void removeApprover(Long approverId, Integer level, Long actorId);
}

