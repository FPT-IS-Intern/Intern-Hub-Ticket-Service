//Version 2
package com.intern.hub.ticket.core.domain.port;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.model.response.ApprovalInfoCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatCardCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatisticsTicketCoreResponse;
import org.springframework.data.repository.query.Param;

public interface TicketRepository {
    TicketModel save(TicketModel model);

    Optional<TicketModel> findById(Long id);

    List<TicketModel> findAll();

    List<TicketModel> findByStatus(TicketStatus status);

    PaginatedData<TicketModel> findAllPaginated(int page, int size);

    PaginatedData<TicketModel> findAllPaginated(int page, int size, List<Long> userIds, String typeName, String status);

    PaginatedData<TicketModel> findAllPaginated(
            int page, int size, List<Long> userIds, String typeName, String status,
            Long startDate, Long endDate, String sortBy, String sortDirection);

    Collection<TicketModel> findByUserId(Long userId);

    Collection<TicketModel> findByUserIdWithFilters(Long userId, String typeName, String status);

    Collection<TicketModel> findTopByUserIdAndTypeNameInOrderByCreatedAtDesc(List<String> typeNames, int limit);

    int rejectTicket(
            Long ticketId,
            TicketStatus status,
            Long updatedBy,
            Long updatedAt,
            Integer version);

    StatCardCoreResponse getStatCardData();

    ApprovalInfoCoreResponse getApprovalInfo(Long ticketId);

    TicketModel getTicketDetail(Long ticketId);

    StatisticsTicketCoreResponse statisticsTicket(List<Long> userIds);
}