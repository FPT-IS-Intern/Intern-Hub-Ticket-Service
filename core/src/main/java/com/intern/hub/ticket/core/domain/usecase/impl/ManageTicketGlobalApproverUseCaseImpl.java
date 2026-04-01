package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;
import java.util.Optional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.ticket.core.domain.model.TicketGlobalApproverModel;
import com.intern.hub.ticket.core.domain.port.TicketGlobalApproverRepository;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketGlobalApproverUseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManageTicketGlobalApproverUseCaseImpl implements ManageTicketGlobalApproverUseCase {

    private final TicketGlobalApproverRepository repository;

    @Override
    public List<Long> getApproverIds(Integer level) {
        int l = normalizeLevel(level);
        return repository.findApproverIdsByMinLevel(l);
    }

    @Override
    public void assignApprover(Long approverId, Integer level, Long actorId) {
        if (approverId == null || approverId <= 0) {
            throw new BadRequestException("bad.request", "ApproverId is required");
        }
        int l = normalizeLevel(level);

        Optional<TicketGlobalApproverModel> existing = repository.findByApproverId(approverId);
        int nextMax = l;
        if (existing.isPresent() && existing.get().getMaxApprovalLevel() != null) {
            nextMax = Math.max(existing.get().getMaxApprovalLevel(), l);
        }

        repository.save(TicketGlobalApproverModel.builder()
                .approverId(approverId)
                .maxApprovalLevel(nextMax)
                .build());
    }

    @Override
    public void removeApprover(Long approverId, Integer level, Long actorId) {
        if (approverId == null || approverId <= 0) {
            throw new BadRequestException("bad.request", "ApproverId is required");
        }
        normalizeLevel(level);
        // Business rule: remove action always removes approval permission completely.
        repository.deleteByApproverId(approverId);
    }

    private int normalizeLevel(Integer level) {
        if (level == null) return 1;
        if (level != 1 && level != 2) {
            throw new BadRequestException("bad.request", "Invalid approval level");
        }
        return level;
    }
}
