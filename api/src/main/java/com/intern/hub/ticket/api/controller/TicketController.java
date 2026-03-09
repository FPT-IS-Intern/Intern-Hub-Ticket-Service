package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.request.LeaveRequestItem;
import com.intern.hub.ticket.api.dto.request.RemoteRequestItem;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import com.intern.hub.ticket.core.domain.command.CancelTicketCommand;
import com.intern.hub.ticket.core.domain.dto.TicketDetailDto;
import com.intern.hub.ticket.core.domain.dto.TicketDto;
import com.intern.hub.ticket.core.domain.dto.TicketTypeDto;
import com.intern.hub.ticket.core.port.in.CancelTicketUseCase;
import com.intern.hub.ticket.core.port.in.GetAllTicketsUseCase;
import com.intern.hub.ticket.core.port.in.GetPendingTicketsUseCase;
import com.intern.hub.ticket.core.port.in.GetTicketDetailUseCase;
import com.intern.hub.ticket.core.port.in.GetTicketTypesUseCase;
import com.intern.hub.ticket.core.port.in.GetUserTicketsUseCase;
import com.intern.hub.ticket.core.port.in.LeaveRequestUseCase;
import com.intern.hub.ticket.core.port.in.RemoteRequestUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final CancelTicketUseCase cancelTicketUseCase;
    private final GetUserTicketsUseCase getUserTicketsUseCase;
    private final GetTicketTypesUseCase getTicketTypesUseCase;
    private final GetTicketDetailUseCase getTicketDetailUseCase;
    private final GetPendingTicketsUseCase getPendingTicketsUseCase;
    private final GetAllTicketsUseCase getAllTicketsUseCase;
    private final LeaveRequestUseCase leaveRequestUseCase;
    private final RemoteRequestUseCase remoteRequestUseCase;
    private final TicketApiMapper mapper;

    @GetMapping("/{ticketId}")
    @Authenticated
    public ResponseApi<TicketDetailDto> getTicketDetail(@PathVariable Long ticketId) {
        return ResponseApi.ok(getTicketDetailUseCase.getTicketDetail(ticketId));
    }

    @GetMapping("/pending")
    @Authenticated
    public ResponseApi<List<TicketDto>> getPendingTickets() {
        return ResponseApi.ok(getPendingTicketsUseCase.getPendingTickets());
    }

    @GetMapping("/all")
    @Authenticated
    public ResponseApi<List<TicketDto>> getAllTickets() {
        return ResponseApi.ok(getAllTicketsUseCase.getAllTickets());
    }

    @GetMapping("/types")
    @Authenticated
    public ResponseApi<List<TicketTypeDto>> getTicketTypes() {
        return ResponseApi.ok(getTicketTypesUseCase.getTicketTypes());
    }

    @GetMapping
    @Authenticated
    public ResponseApi<List<TicketDto>> getUserTickets(@RequestHeader("X-User-Id") Long userId) {
        return ResponseApi.ok(getUserTicketsUseCase.getUserTickets(userId));
    }

    @PostMapping("/{ticketId}/cancel")
    @Authenticated
    public ResponseApi<Void> cancelTicket(@PathVariable Long ticketId, @RequestHeader("X-User-Id") Long requesterId) {
        CancelTicketCommand command = CancelTicketCommand.builder()
                .ticketId(ticketId)
                .requesterId(requesterId)
                .build();

        cancelTicketUseCase.cancelTicket(command);
        return ResponseApi.ok(null);
    }

    @PostMapping("/leave")
    @ResponseStatus(HttpStatus.CREATED)
    // @Authenticated
    public ResponseApi<TicketDto> createLeaveRequest(
            @Valid @RequestBody LeaveRequestItem request,
            @RequestHeader("X-User-Id") Long userId) {
        var command = mapper.toCommand(request, userId);
        TicketDto ticketDto = leaveRequestUseCase.createLeaveRequest(command);
        return ResponseApi.ok(ticketDto);
    }

    @PostMapping("/remote")
    @ResponseStatus(HttpStatus.CREATED)
    // @Authenticated
    public ResponseApi<TicketDto> createRemoteRequest(
            @Valid @RequestBody RemoteRequestItem request,
            @RequestHeader("X-User-Id") Long userId) {
        var command = mapper.toCommand(request, userId);
        TicketDto ticketDto = remoteRequestUseCase.createRemoteRequest(command);
        return ResponseApi.ok(ticketDto);
    }
}
