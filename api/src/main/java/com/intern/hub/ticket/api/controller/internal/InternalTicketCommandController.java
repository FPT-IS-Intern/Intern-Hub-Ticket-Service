package com.intern.hub.ticket.api.controller.internal;


import com.intern.hub.ticket.core.domain.model.response.TicketDetailResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
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
    private final TicketApiMapper ticketApiMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Internal
    public ResponseApi<TicketResponse> createTicketInternal(
            @RequestPart("creatorId") Long creatorId,
            @RequestPart("request") @Valid CreateTicketRequest request,
            @RequestPart(value = "evidences", required = false) MultipartFile[] evidences) {

        CreateTicketCommand command = new CreateTicketCommand(
                creatorId,
                request.ticketTypeId(),
                request.payload(),
                evidences);

        TicketModel createdTicket = ticketUsecase.create(command);
        return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
    }

    @GetMapping("/{ticketId}")
    @Internal
    public ResponseApi<TicketDetailResponse> getTicketDetail(@PathVariable Long ticketId) {

        TicketDetailResponse response = ticketUsecase.getTicketDetail(ticketId);

        return ResponseApi.ok(response);
    }
}
