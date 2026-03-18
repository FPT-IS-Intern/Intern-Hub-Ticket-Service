package com.intern.hub.ticket.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.HasPermission;
import com.intern.hub.starter.security.entity.Action;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/ticket-types/{ticketTypeId}/approvers")
@RequiredArgsConstructor
public class AdminTicketTypeApproverController {

    private final ManageTicketTypeApproverUseCase manageUseCase;

    @PostMapping("/{approverId}")
    //@HasPermission(resource = "ticket-type", action = Action.UPDATE)
    public ResponseApi<?> assignApprover(
            @PathVariable Long ticketTypeId,
            @PathVariable Long approverId) {
        
        //Long adminId = UserContext.requiredUserId();
        Long adminId = 0L;
        manageUseCase.assignApprover(ticketTypeId, approverId, adminId);
        return ResponseApi.noContent();
    }

    @DeleteMapping("/{approverId}")
    //@HasPermission(resource = "ticket-type", action = Action.UPDATE)
    public ResponseApi<?> removeApprover(
            @PathVariable Long ticketTypeId,
            @PathVariable Long approverId) {
        
        manageUseCase.removeApprover(ticketTypeId, approverId);
        return ResponseApi.noContent();
    }
}