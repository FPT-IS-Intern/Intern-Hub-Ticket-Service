package com.intern.hub.ticket.core.domain.usecase;

import java.util.Collection;
import java.util.List;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.response.StatCardCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatisticsTicketCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.TicketDetailResponse;

public interface TicketUsecase {
    TicketModel create(CreateTicketCommand command);

    TicketDetailResponse getTicketDetail(Long ticketId);

    List<TicketModel> getPendingTickets();

    PaginatedData<TicketModel> getAllTickets(int page, int size);

    PaginatedData<TicketModel> getAllTickets(int page, int size, String nameOrEmail, String typeName, String status);

    Collection<TicketModel> getMyTickets(Long userId);

    /**
     * Lấy tickets cho trang quản lý phiếu — đã enrich với fullName, email (từ HRM)
     * và typeName (từ TicketType). Dùng chung filter params với getAllTickets.
     */
    PaginatedData<TicketModel> getAllTicketsForManagement(
            int page, int size, String nameOrEmail, String typeName, String status,
            Long startDate, Long endDate, String sortBy, String sortDirection);

    StatCardCoreResponse getStatCardData();

    StatisticsTicketCoreResponse statisticsTicket();
}
