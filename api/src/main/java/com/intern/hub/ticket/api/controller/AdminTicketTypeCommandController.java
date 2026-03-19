package com.intern.hub.ticket.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.request.CreateTicketTypeRequest;
import com.intern.hub.ticket.api.dto.response.TicketTypeResponse;
import com.intern.hub.ticket.core.domain.model.ApprovalRule;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketTypeUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/ticket-types")
@RequiredArgsConstructor
public class AdminTicketTypeCommandController {

    private final CreateTicketTypeUseCase createTicketTypeUseCase;

    @PostMapping
    // @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.CREATE)
    public ResponseApi<TicketTypeResponse> createTicketType(
            @Valid @RequestBody CreateTicketTypeRequest request) {

        ApprovalRule ruleModel = null;
        if (request.approvalRule() != null) {
            ruleModel = ApprovalRule.builder()
                    .condition(request.approvalRule().condition())
                    .levelsIfTrue(request.approvalRule().levelsIfTrue())
                    .levelsIfFalse(request.approvalRule().levelsIfFalse())
                    .build();
        }

        CreateTicketTypeCommand command = new CreateTicketTypeCommand(
                request.typeName(),
                request.description(),
                request.template(),
                ruleModel);

        var createdType = createTicketTypeUseCase.create(command);

        return ResponseApi.ok(new TicketTypeResponse(createdType.getTicketTypeId(), createdType.getTypeName()));
    }

}