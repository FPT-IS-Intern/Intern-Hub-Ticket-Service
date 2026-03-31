package com.intern.hub.ticket.infra.mapper;

import com.intern.hub.ticket.core.domain.model.response.RolePermissionCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.RolePermissionResponse;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.UserRoleInfraResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthIdentityFeignMapper {

    UserRoleCoreResponse toUserRoleCoreResponse(UserRoleInfraResponse userRoleInfraResponse);

    @Mapping(source = "resource.id", target = "resourceId")
    RolePermissionCoreResponse toRolePermissionCoreResponse(RolePermissionResponse rolePermissionResponse);

    List<RolePermissionCoreResponse> toRolePermissionCoreResponseList(List<RolePermissionResponse> rolePermissionResponses);
}
