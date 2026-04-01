package com.intern.hub.ticket.infra.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.response.RolePermissionCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;
import com.intern.hub.ticket.core.domain.port.AuthIdentityPort;
import com.intern.hub.ticket.core.domain.port.TicketGlobalApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketTaskPermissionAdapter implements TicketTaskPermissionPort {

    private static final String REVIEW_PERMISSION = "review";

    private final TicketTypeApproverRepository ticketTypeApproverRepository;
    private final TicketGlobalApproverRepository ticketGlobalApproverRepository;
    private final AuthIdentityPort authIdentityPort;

    @Value("${ticket.approval.level1-resource-id:0}")
    private Long level1ResourceId;

    @Value("${ticket.approval.level2-resource-id:162752348429488128}")
    private Long level2ResourceId;

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

        UserRoleCoreResponse userRole = authIdentityPort.getRoleByUserId(approverId);
        if (userRole == null || userRole.getId() == null) {
            return false;
        }

        Long roleId;
        try {
            roleId = Long.parseLong(userRole.getId());
        } catch (NumberFormatException ex) {
            return false;
        }

        List<RolePermissionCoreResponse> permissions = authIdentityPort.getRolePermissions(roleId);
        boolean hasLevel1Permission = hasReviewPermission(permissions, level1ResourceId);
        boolean hasLevel2Permission = hasReviewPermission(permissions, level2ResourceId);

        if (currentLevel <= 1) {
            return assignedLevel1 && (hasLevel1Permission || hasLevel2Permission);
        }

        return assignedLevel2 && hasLevel2Permission;
    }

    private boolean hasReviewPermission(List<RolePermissionCoreResponse> permissions, Long resourceId) {
        if (resourceId == null || resourceId <= 0 || permissions == null) {
            return false;
        }
        return permissions.stream()
                .anyMatch(p -> resourceId.equals(p.getResourceId())
                        && p.getPermissions() != null
                        && p.getPermissions().contains(REVIEW_PERMISSION));
    }
}
