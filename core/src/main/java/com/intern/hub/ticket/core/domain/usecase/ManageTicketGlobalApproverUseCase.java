package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

public interface ManageTicketGlobalApproverUseCase {
    /**
     * @param level 1 or 2. Level 1 returns all approvers. Level 2 returns approvers with maxApprovalLevel=2.
     */
    List<Long> getApproverIds(Integer level);

    /**
     * Returns max approval level of the approver.
     * 0 means user has no approval permission.
     */
    int getApproverLevel(Long approverId);

    /**
     * Ensure approver has at least the given level (1 or 2). If level=2, it upgrades to level 2.
     */
    void assignApprover(Long approverId, Integer level, Long actorId);

    /**
     * Remove approval right entirely (for both level=1 and level=2 actions).
     */
    void removeApprover(Long approverId, Integer level, Long actorId);
}
