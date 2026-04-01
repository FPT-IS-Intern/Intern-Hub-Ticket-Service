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
        int l = normalizeLevel(level);

        Optional<TicketGlobalApproverModel> existing = repository.findByApproverId(approverId);
        if (existing.isEmpty()) return;

        Integer current = existing.get().getMaxApprovalLevel();
        if (current == null) {
            repository.deleteByApproverId(approverId);
            return;
        }

        if (l <= 1) {
            // Remove completely (also removes level2)
            repository.deleteByApproverId(approverId);
            return;
        }

        // level2 removal -> downgrade to level1 if currently level2
        if (current >= 2) {
            repository.save(TicketGlobalApproverModel.builder()
                    .approverId(approverId)
                    .maxApprovalLevel(1)
                    .build());
        }
    }

    private int normalizeLevel(Integer level) {
        if (level == null) return 1;
        if (level != 1 && level != 2) {
            throw new BadRequestException("bad.request", "Invalid approval level");
        }
        return level;
    }
}

