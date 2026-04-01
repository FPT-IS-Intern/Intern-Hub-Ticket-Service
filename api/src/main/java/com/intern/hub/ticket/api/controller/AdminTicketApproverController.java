package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.response.ApproverIdsResponse;
import com.intern.hub.ticket.api.dto.response.ApproverPermissionResponse;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketGlobalApproverUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/approvers")
@RequiredArgsConstructor
public class AdminTicketApproverController {

    private final ManageTicketGlobalApproverUseCase useCase;

    @GetMapping
    public ResponseApi<ApproverIdsResponse> getApproverIds(
            @RequestParam(value = "level", required = false) Integer level) {
        List<Long> ids = useCase.getApproverIds(level);
        return ResponseApi.ok(new ApproverIdsResponse(ids));
    }

    @GetMapping("/me")
    public ResponseApi<ApproverPermissionResponse> getMyApproverPermission() {
        Long userId = UserContext.requiredUserId();
        int maxApprovalLevel = useCase.getApproverLevel(userId);
        return ResponseApi.ok(new ApproverPermissionResponse(
                userId,
                maxApprovalLevel,
                maxApprovalLevel >= 1,
                maxApprovalLevel >= 2
        ));
    }

    @PostMapping("/{approverId}")
    public ResponseApi<?> assignApprover(
            @PathVariable Long approverId,
            @RequestParam(value = "level", required = false) Integer level) {
        Long actorId = resolveActorId();
        useCase.assignApprover(approverId, level, actorId);
        return ResponseApi.noContent();
    }

    @DeleteMapping("/{approverId}")
    public ResponseApi<?> removeApprover(
            @PathVariable Long approverId,
            @RequestParam(value = "level", required = false) Integer level) {
        Long actorId = resolveActorId();
        useCase.removeApprover(approverId, level, actorId);
        return ResponseApi.noContent();
    }

    private Long resolveActorId() {
        // Feign internal calls from bo-portal may not propagate end-user principal.
        // For this admin-config endpoint, fallback to system actor id when principal is absent.
        return UserContext.userId().orElse(0L);
    }
}
