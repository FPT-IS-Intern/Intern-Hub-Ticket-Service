package com.intern.hub.ticket.infra.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.infra.persistence.entity.Evidence;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EvidenceMapper {
    EvidenceModel toModel(Evidence entity);

    Evidence toEntity(EvidenceModel model);

    List<EvidenceModel> toModels(List<Evidence> entities);

    @AfterMapping
    default void mapAuditFields(EvidenceModel model, @MappingTarget Evidence entity) {
        if (model == null || entity == null)
            return;
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());
    }
}
