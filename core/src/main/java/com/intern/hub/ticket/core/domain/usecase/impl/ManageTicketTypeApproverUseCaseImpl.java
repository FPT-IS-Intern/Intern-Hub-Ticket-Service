package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManageTicketTypeApproverUseCaseImpl implements ManageTicketTypeApproverUseCase {

    private final TicketTypeApproverRepository approverRepository;
    private final Snowflake snowflake;

    @Override
    @Transactional
    public void assignApprover(Long ticketTypeId, Long approverId, Integer approvalLevel, Long adminId) {
        int level = approvalLevel == null ? 1 : approvalLevel;
        if (level < 1) level = 1;
        if (level > 2) level = 2;

        // Level 2 implies level 1.
        if (level == 2) {
            ensureAssigned(ticketTypeId, approverId, 1);
            ensureAssigned(ticketTypeId, approverId, 2);
            return;
        }

        // Assign level 1 only if user doesn't already have level 2.
        List<Integer> levels = approverRepository.findApprovalLevels(ticketTypeId, approverId);
        if (levels != null && levels.contains(2)) return;

        ensureAssigned(ticketTypeId, approverId, 1);
    }

    @Override
    public void removeApprover(Long ticketTypeId, Long approverId, Integer approvalLevel) {
        approverRepository.delete(ticketTypeId, approverId, approvalLevel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getApproverIds(Long ticketTypeId, Integer approvalLevel) {
        if (approvalLevel == null) {
            return approverRepository.findApproverIdsByTicketTypeId(ticketTypeId);
        }
        return approverRepository.findApproverIdsByTicketTypeIdAndApprovalLevel(ticketTypeId, approvalLevel);
    }

    private void ensureAssigned(Long ticketTypeId, Long approverId, int level) {
        if (approverRepository.exists(ticketTypeId, approverId, level)) return;

        TicketTypeApproverModel model = TicketTypeApproverModel.builder()
                .ticketTypeApproverId(snowflake.next())
                .ticketTypeId(ticketTypeId)
                .approverId(approverId)
                .approvalLevel(level)
                .build();

        approverRepository.save(model);
    }
}

