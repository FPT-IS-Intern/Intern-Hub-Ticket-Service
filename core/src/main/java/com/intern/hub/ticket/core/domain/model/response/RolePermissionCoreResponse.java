package com.intern.hub.ticket.core.domain.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionCoreResponse {
    Long resourceId;
    List<String> permissions;
}
