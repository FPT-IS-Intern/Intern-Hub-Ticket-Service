package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.TicketType;
import com.intern.hub.ticket.infra.persistence.entity.TicketTypeEntity;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {
    TicketType toDomain(TicketTypeEntity entity);

    TicketTypeEntity toEntity(TicketType domain);
}
