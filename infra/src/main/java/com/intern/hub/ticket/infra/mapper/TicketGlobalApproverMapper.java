package com.intern.hub.ticket.infra.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.TicketGlobalApproverModel;
import com.intern.hub.ticket.infra.persistence.entity.TicketGlobalApprover;

@Mapper(componentModel = "spring")
public interface TicketGlobalApproverMapper {
    TicketGlobalApprover toEntity(TicketGlobalApproverModel model);
    TicketGlobalApproverModel toModel(TicketGlobalApprover entity);
}

