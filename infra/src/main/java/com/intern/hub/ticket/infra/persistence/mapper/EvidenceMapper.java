package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.Evidence;
import com.intern.hub.ticket.infra.persistence.entity.EvidenceEntity;

@Mapper(componentModel = "spring")
public interface EvidenceMapper {
    Evidence toDomain(EvidenceEntity entity);

    EvidenceEntity toEntity(Evidence domain);
}
