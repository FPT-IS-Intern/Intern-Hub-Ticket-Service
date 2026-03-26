package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.response.UserRoleCoreResponse;

public interface AuthIdentityPort {

    UserRoleCoreResponse getRoleByUserId(Long userId);
}
