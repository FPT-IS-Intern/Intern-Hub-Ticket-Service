package com.intern.hub.ticket.infra.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;
import com.intern.hub.ticket.infra.persistence.entity.TicketTypeApprover;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketTypeApproverMapper {

    TicketTypeApproverModel toModel(TicketTypeApprover entity);
    TicketTypeApprover toEntity(TicketTypeApproverModel model);

    @AfterMapping
    default void mapAuditFields(TicketTypeApproverModel model, @MappingTarget TicketTypeApprover entity) {
        if (model == null || entity == null) return;
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());
    }
}