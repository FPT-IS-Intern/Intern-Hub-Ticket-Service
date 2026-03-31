package com.intern.hub.ticket.infra.adapter;

import com.intern.hub.ticket.core.domain.model.response.RolePermissionCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;
import com.intern.hub.ticket.core.domain.port.AuthIdentityPort;
import com.intern.hub.ticket.infra.feignClient.client.AuthIdentityFeignClient;
import com.intern.hub.ticket.infra.mapper.AuthIdentityFeignMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthIdentityAdapter implements AuthIdentityPort {

    AuthIdentityFeignClient authIdentityFeignClient;

    AuthIdentityFeignMapper authIdentityFeignMapper;

    @Override
    public UserRoleCoreResponse getRoleByUserId(Long userId) {
        return authIdentityFeignMapper.toUserRoleCoreResponse(authIdentityFeignClient.getRoleByUserId(userId).data());
    }

    @Override
    public List<RolePermissionCoreResponse> getRolePermissions(Long roleId) {
        return authIdentityFeignMapper.toRolePermissionCoreResponseList(authIdentityFeignClient.getRolePermissions(roleId).data());
    }
}
