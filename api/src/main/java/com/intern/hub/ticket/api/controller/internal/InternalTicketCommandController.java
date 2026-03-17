package com.intern.hub.ticket.api.controller.internal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${security.internal-path-prefix:/ticket/internal}/v1/tickets")
@RequiredArgsConstructor
public class InternalTicketCommandController {

    private final CreateTicketUsecase createTicketUsecase;

    @PostMapping
    @Internal
    public ResponseApi<TicketResponse> createTicketInternal(
            @Valid @RequestBody CreateTicketRequest request) {

        Long systemUserId = 0L;

        CreateTicketCommand command = new CreateTicketCommand(systemUserId, request.ticketTypeId(), request.payload());
        TicketModel createdTicket = createTicketUsecase.create(command);

        return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
    }

    

}
