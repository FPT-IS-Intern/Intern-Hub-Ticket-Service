package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.TicketApproval;
import com.intern.hub.ticket.infra.persistence.entity.TicketApprovalEntity;

@Mapper(componentModel = "spring")
public interface TicketApprovalMapper {
    TicketApproval toDomain(TicketApprovalEntity entity);

    TicketApprovalEntity toEntity(TicketApproval domain);
}
