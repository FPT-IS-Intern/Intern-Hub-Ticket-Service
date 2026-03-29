package com.intern.hub.ticket.api.mapper;

import java.util.List;

import com.intern.hub.ticket.api.dto.response.*;
import com.intern.hub.ticket.core.domain.model.response.StatCardCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.StatisticsTicketCoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.response.ApprovalInfoCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.TicketDetailResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TicketApiMapper {

    TicketDto toDto(TicketModel model);

    List<TicketDto> toDtoList(List<TicketModel> models);

    TicketDetailDto toDetailDto(TicketModel model);

    @Mapping(target = "items", source = "items")
    PaginatedData<TicketDto> toPaginatedDto(PaginatedData<TicketModel> modelPage);

    /**
     * Map TicketModel (có fullName, email từ HRM và typeName từ TicketType)
     * sang DTO dành cho trang Quản lý phiếu.
     * Transient fields: fullName, email, typeName — KHÔNG map vào Ticket entity.
     */
    TicketManagementDto toManagementDto(TicketModel model);

    List<TicketManagementDto> toManagementDtoList(List<TicketModel> models);

    @Mapping(target = "items", source = "items")
    PaginatedData<TicketManagementDto> toPaginatedManagementDto(PaginatedData<TicketModel> modelPage);

    StatCardApiResponse toStatCardApiResponse(StatCardCoreResponse coreResponse);

    TicketDetailDto toDetailDto(TicketDetailResponse response);

    TicketDetailResponseDto toDetailResponseDto(TicketDetailResponse response);

    TicketApprovalInfoDto toApprovalInfoDto(ApprovalInfoCoreResponse response);

    TicketStatisticApiResponse toTicketStatisticApiResponse(StatisticsTicketCoreResponse coreResponse);
}
