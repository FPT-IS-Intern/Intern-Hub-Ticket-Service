package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.response.ApproverIdsResponse;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/ticket-types/{ticketTypeId}/approvers")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4221")
public class AdminTicketTypeApproverController {

    private final ManageTicketTypeApproverUseCase manageUseCase;

    @PostMapping("/{approverId}")
//    @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.UPDATE)
    public ResponseApi<?> assignApprover(
            @PathVariable Long ticketTypeId,
            @PathVariable Long approverId) {

        Long adminId = UserContext.requiredUserId();
        //Long adminId = 1L;

        manageUseCase.assignApprover(ticketTypeId, approverId, adminId);
        return ResponseApi.noContent();
    }

    @DeleteMapping("/{approverId}")
//    @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.UPDATE)
    public ResponseApi<?> removeApprover(
            @PathVariable Long ticketTypeId,
            @PathVariable Long approverId) {

        manageUseCase.removeApprover(ticketTypeId, approverId);
        return ResponseApi.noContent();
    }

    @GetMapping
//    @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.READ)
    public ResponseApi<ApproverIdsResponse> getApproverIds(@PathVariable Long ticketTypeId) {
        List<Long> approverIds = manageUseCase.getApproverIds(ticketTypeId);
        return ResponseApi.ok(new ApproverIdsResponse(approverIds));
    }
}
