package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.request.CreateTicketTypeRequest;
import com.intern.hub.ticket.api.dto.request.UpdateTicketTypeRequest;
import com.intern.hub.ticket.api.dto.response.TicketTypeResponse;
import com.intern.hub.ticket.core.domain.model.ApprovalRule;
import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.model.command.UpdateTicketTypeCommand;
import com.intern.hub.ticket.core.domain.usecase.TicketTypeUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/ticket-types")
@RequiredArgsConstructor
public class AdminTicketTypeCommandController {

    private final TicketTypeUseCase ticketTypeUseCase;

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

        var createdType = ticketTypeUseCase.create(command);

        return ResponseApi.ok(new TicketTypeResponse(createdType.getTicketTypeId(), createdType.getTypeName()));
    }

    @PatchMapping("/{ticketTypeId}")
    // @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.UPDATE)
    public ResponseApi<TicketTypeResponse> updateTicketType(
            @PathVariable Long ticketTypeId,
            @Valid @RequestBody UpdateTicketTypeRequest request) {

        ApprovalRule ruleModel = null;
        if (request.approvalRule() != null) {
            ruleModel = ApprovalRule.builder()
                    .condition(request.approvalRule().condition())
                    .levelsIfTrue(request.approvalRule().levelsIfTrue())
                    .levelsIfFalse(request.approvalRule().levelsIfFalse())
                    .build();
        }

        UpdateTicketTypeCommand command = new UpdateTicketTypeCommand(
                ticketTypeId,
                request.typeName(),
                request.description(),
                request.template(),
                ruleModel);

        var updatedType = ticketTypeUseCase.update(command);

        return ResponseApi.ok(new TicketTypeResponse(updatedType.getTicketTypeId(), updatedType.getTypeName()));
    }

    @DeleteMapping("/{ticketTypeId}")
    // @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.DELETE)
    public ResponseApi<Void> deleteTicketType(@PathVariable Long ticketTypeId) {
        ticketTypeUseCase.delete(ticketTypeId);
        return ResponseApi.ok(null);
    }

    @GetMapping("/{ticketTypeId}")
    // @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.READ)
    public ResponseApi<TicketTypeResponse> getTicketType(@PathVariable Long ticketTypeId) {
        TicketTypeModel model = ticketTypeUseCase.getById(ticketTypeId);
        return ResponseApi.ok(new TicketTypeResponse(model.getTicketTypeId(), model.getTypeName()));
    }

    @GetMapping("/all")
    // @Authenticated
    // @HasPermission(resource = "ticket-type", action = Action.READ)
    public ResponseApi<List<TicketTypeResponse>> getAllTicketTypes() {
        List<TicketTypeResponse> response = ticketTypeUseCase.getAll().stream()
                .map(model -> new TicketTypeResponse(model.getTicketTypeId(), model.getTypeName()))
                .collect(Collectors.toList());
        return ResponseApi.ok(response);
    }

}