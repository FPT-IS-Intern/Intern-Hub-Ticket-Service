package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(target = "ticketTypeName", source = "ticketType.typeName")
    Ticket toDomain(TicketEntity entity);

    @Mapping(target = "ticketType", ignore = true)
    TicketEntity toEntity(Ticket domain);
}
