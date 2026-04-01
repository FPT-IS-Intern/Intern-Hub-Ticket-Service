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

    @PostMapping("/{approverId}")
    public ResponseApi<?> assignApprover(
            @PathVariable Long approverId,
            @RequestParam(value = "level", required = false) Integer level) {
        Long actorId = UserContext.requiredUserId();
        useCase.assignApprover(approverId, level, actorId);
        return ResponseApi.noContent();
    }

    @DeleteMapping("/{approverId}")
    public ResponseApi<?> removeApprover(
            @PathVariable Long approverId,
            @RequestParam(value = "level", required = false) Integer level) {
        Long actorId = UserContext.requiredUserId();
        useCase.removeApprover(approverId, level, actorId);
        return ResponseApi.noContent();
    }
}

