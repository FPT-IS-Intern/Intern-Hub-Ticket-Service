package com.intern.hub.ticket.api.controller.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${security.internal-path-prefix:/ticket/internal}")
@RequiredArgsConstructor
public class InternalTicketCommandController {

    private final TicketUsecase ticketUsecase;

    @PostMapping
    @Internal
    public ResponseApi<TicketResponse> createTicketInternal(
            @Valid @RequestBody CreateTicketRequest request) {

        Long systemUserId = 0L;

        CreateTicketCommand command = new CreateTicketCommand(systemUserId, request.ticketTypeId(), request.payload());
        TicketModel createdTicket = ticketUsecase.create(command);

        return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
    }

    @GetMapping("/{ticketId}")
    @Internal
    public ResponseApi<TicketDetailDto> getTicketDetailInternal(@PathVariable Long ticketId) {
        TicketModel model = ticketUsecase.getTicketDetail(ticketId);

        TicketDetailDto detailDto = new TicketDetailDto(
                model.getTicketId(),
                model.getUserId(),
                model.getTicketTypeId(),
                model.getStatus(),
                model.getPayload(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getCreatedBy(),
                model.getUpdatedBy());
        return ResponseApi.ok(detailDto);
    }

}
