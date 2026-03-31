package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.response.RolePermissionCoreResponse;
import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;

import java.util.List;

public interface AuthIdentityPort {

    UserRoleCoreResponse getRoleByUserId(Long userId);

    List<RolePermissionCoreResponse> getRolePermissions(Long roleId);

}
