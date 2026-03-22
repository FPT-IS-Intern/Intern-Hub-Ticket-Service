package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.starter.security.annotation.HasPermission;
import com.intern.hub.starter.security.entity.Action;
import com.intern.hub.ticket.api.dto.request.FilterTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketDto;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketQueryController {

    private final TicketUsecase ticketUsecase;
    private final TicketApiMapper ticketApiMapper;

    @GetMapping("/all")
    @Authenticated
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<PaginatedData<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            FilterTicketRequest filter) {

        PaginatedData<TicketModel> modelPage = ticketUsecase.getAllTickets(
                page, size, filter.getNameOrEmail(), filter.getTypeName(), filter.getStatus());
        return ResponseApi.ok(ticketApiMapper.toPaginatedDto(modelPage));
    }

    @GetMapping("/pending")
    @Authenticated
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<List<TicketDto>> getPendingTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TicketModel> models = ticketUsecase.getPendingTickets();
        return ResponseApi.ok(ticketApiMapper.toDtoList(models));
    }

    @GetMapping("/{ticketId}")
    @Authenticated
    @HasPermission(action = Action.READ, resource = "ticket")
    public ResponseApi<TicketDetailDto> getTicketDetail(@PathVariable Long ticketId) {
        TicketModel model = ticketUsecase.getTicketDetail(ticketId);
        return ResponseApi.ok(ticketApiMapper.toDetailDto(model));
    }

    @GetMapping("/me")
    @Authenticated
    public ResponseApi<List<TicketDto>> getMyTickets() {
        Long userId = UserContext.requiredUserId();
        List<TicketModel> models = (List<TicketModel>) ticketUsecase.getMyTickets(userId);
        return ResponseApi.ok(ticketApiMapper.toDtoList(models));
    }
}