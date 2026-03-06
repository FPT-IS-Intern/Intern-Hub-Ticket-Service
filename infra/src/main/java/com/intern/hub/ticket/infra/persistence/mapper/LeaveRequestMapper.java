package com.intern.hub.ticket.infra.persistence.mapper;

import org.mapstruct.Mapper;

import com.intern.hub.ticket.core.domain.model.LeaveRequest;
import com.intern.hub.ticket.infra.persistence.entity.LeaveRequestEntity;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {
    LeaveRequest toDomain(LeaveRequestEntity entity);

    LeaveRequestEntity toEntity(LeaveRequest domain);
}
