package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.Ticket;
import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toDomain(TicketEntity entity);

    TicketEntity toEntity(Ticket domain);
}
