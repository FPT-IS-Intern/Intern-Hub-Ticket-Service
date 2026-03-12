package com.intern.hub.ticket.infra.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;

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
        entity.setVersion(model.getVersion());
    }
}
