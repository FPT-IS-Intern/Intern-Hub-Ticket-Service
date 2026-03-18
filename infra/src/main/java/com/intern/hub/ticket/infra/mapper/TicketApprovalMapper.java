package com.intern.hub.ticket.infra.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;
import com.intern.hub.ticket.infra.persistence.entity.TicketApproval;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketApprovalMapper {

    TicketApprovalModel toModel(TicketApproval entity);

    TicketApproval toEntity(TicketApprovalModel model);

    @AfterMapping
    default void mapAuditFields(TicketApprovalModel model, @MappingTarget TicketApproval entity) {
        if (model == null || entity == null)
            return;

        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());
    }

    @AfterMapping
    default void mapAuditFieldsToModel(TicketApproval entity, @MappingTarget TicketApprovalModel model) {
        if (entity == null || model == null) return;

        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedBy(entity.getUpdatedBy());
    }
}
