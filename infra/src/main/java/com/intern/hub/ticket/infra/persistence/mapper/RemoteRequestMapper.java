package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.RemoteRequest;
import com.intern.hub.ticket.infra.persistence.entity.RemoteRequestEntity;

@Mapper(componentModel = "spring")
public interface RemoteRequestMapper {
    RemoteRequest toDomain(RemoteRequestEntity entity);

    RemoteRequestEntity toEntity(RemoteRequest domain);
}
