package com.intern.hub.ticket.api.controller.internal;

import java.util.List;

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
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.EvidenceCommand;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${security.internal-path-prefix:/ticket/internal}")
@RequiredArgsConstructor
public class InternalTicketCommandController {

        private final TicketUsecase ticketUsecase;
        private final TicketApiMapper ticketApiMapper;

        @PostMapping
        @Internal
        public ResponseApi<TicketResponse> createTicketInternal(
                        @Valid @RequestBody CreateTicketRequest request) {

                Long systemUserId = 0L;

                List<EvidenceCommand> evidenceCommands = request.evidences() == null ? List.of()
                                : request.evidences().stream()
                                                .map(e -> new EvidenceCommand(e.evidenceKey(), e.fileType(),
                                                                e.fileSize()))
                                                .toList();

                CreateTicketCommand command = new CreateTicketCommand(systemUserId, request.ticketTypeId(),
                                request.payload(),
                                evidenceCommands);
                TicketModel createdTicket = ticketUsecase.create(command);

                return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
        }

        @GetMapping("/{ticketId}")
        @Internal
        public ResponseApi<TicketDetailDto> getTicketDetailInternal(@PathVariable Long ticketId) {
                TicketModel model = ticketUsecase.getTicketDetail(ticketId);
                return ResponseApi.ok(ticketApiMapper.toDetailDto(model));
        }

}
