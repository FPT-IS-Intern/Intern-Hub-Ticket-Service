package com.intern.hub.ticket.core.domain.port;

import java.util.List;

public interface HrmServicePort {
    List<Long> searchUsers(String nameOrEmail);
}
