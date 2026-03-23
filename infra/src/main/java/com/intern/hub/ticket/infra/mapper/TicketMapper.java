package com.intern.hub.ticket.infra.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;
import com.intern.hub.ticket.infra.persistence.entity.TicketType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketModel toModel(Ticket entity);

    Ticket toEntity(TicketModel model);

    List<TicketModel> toModels(List<Ticket> entities);

    @AfterMapping
    default void mapAuditFields(TicketModel model, @MappingTarget Ticket entity) {
        if (model == null || entity == null)
            return;

        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());

        // Map ticketTypeId to a TicketType entity reference for the @ManyToOne relationship
        // This is required because the ticketTypeId column is insertable=false on the entity
        if (model.getTicketTypeId() != null) {
            TicketType ticketType = new TicketType();
            ticketType.setTicketTypeId(model.getTicketTypeId());
            entity.setTicketType(ticketType);
        }
    }

    @AfterMapping
    default void mapAuditFieldsToModel(Ticket entity, @MappingTarget TicketModel model) {
        if (entity == null || model == null) return;

        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedBy(entity.getUpdatedBy());
    }

    default PaginatedData<TicketModel> toPaginatedModel(org.springframework.data.domain.Page<Ticket> page) {
        if (page == null || page.isEmpty()) {
            return PaginatedData.empty();
        }
        return PaginatedData.<TicketModel>builder()
                .items(toModels(page.getContent())) 
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
