package com.intern.hub.ticket.infra.mapper;

import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.UserRoleInfraResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthIdentityFeignMapper {

    UserRoleCoreResponse toUserRoleCoreResponse(UserRoleInfraResponse userRoleInfraResponse);
}
