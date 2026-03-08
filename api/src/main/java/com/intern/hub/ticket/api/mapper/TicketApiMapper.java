package com.intern.hub.ticket.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.intern.hub.ticket.api.dto.request.LeaveRequestItem;
import com.intern.hub.ticket.api.dto.request.RemoteRequestItem;
import com.intern.hub.ticket.core.domain.command.CreateLeaveRequestCommand;
import com.intern.hub.ticket.core.domain.command.CreateRemoteRequestCommand;

@Mapper(componentModel = "spring")
public interface TicketApiMapper {

    @Mapping(target = "userId", source = "userId")
    CreateLeaveRequestCommand toCommand(LeaveRequestItem requestItem, Long userId);

    @Mapping(target = "userId", source = "userId")
    CreateRemoteRequestCommand toCommand(RemoteRequestItem requestItem, Long userId);
}
