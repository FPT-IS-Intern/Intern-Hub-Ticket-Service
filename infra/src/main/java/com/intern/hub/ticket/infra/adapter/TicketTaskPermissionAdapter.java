package com.intern.hub.ticket.infra.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.port.TicketGlobalApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketTaskPermissionAdapter implements TicketTaskPermissionPort {

    private final TicketTypeApproverRepository ticketTypeApproverRepository;
    private final TicketGlobalApproverRepository ticketGlobalApproverRepository;

    @Override
    public boolean hasPermission(Long ticketId, Long ticketTypeId, Long approverId, int currentLevel) {
        boolean assignedLevel1 = false;
        boolean assignedLevel2 = false;

        // Prefer global approver config. Fallback to per-ticket-type config for backward compatibility.
        com.intern.hub.ticket.core.domain.model.TicketGlobalApproverModel global =
                ticketGlobalApproverRepository.findByApproverId(approverId).orElse(null);
        if (global != null && global.getMaxApprovalLevel() != null && global.getMaxApprovalLevel() > 0) {
            assignedLevel1 = true;
            assignedLevel2 = global.getMaxApprovalLevel() >= 2;
        } else {
            if (ticketTypeId == null || ticketTypeId <= 0) return false;
            List<Integer> levels = ticketTypeApproverRepository.findApprovalLevels(ticketTypeId, approverId);
            if (levels == null || levels.isEmpty()) return false;
            assignedLevel1 = levels.contains(1) || levels.contains(2);
            assignedLevel2 = levels.contains(2);
        }

        if (currentLevel <= 1) {
            return assignedLevel1;
        }

        return assignedLevel2;
    }
}
