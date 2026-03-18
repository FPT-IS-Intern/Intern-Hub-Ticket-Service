package com.intern.hub.ticket.infra.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.infra.persistence.entity.TicketType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketTypeMapper {

    TicketTypeModel toModel(TicketType entity);
    
    TicketType toEntity(TicketTypeModel model);

    List<TicketTypeModel> toModels(List<TicketType> entities);

    @AfterMapping
    default void mapAuditFields(TicketTypeModel model, @MappingTarget TicketType entity) {
        if (model == null || entity == null)
            return;

        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());
    }
    @AfterMapping
    default void mapAuditFieldsToModel(TicketType entity, @MappingTarget TicketTypeModel model) {
        if (entity == null || model == null) return;

        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedBy(entity.getUpdatedBy());
    }
}
