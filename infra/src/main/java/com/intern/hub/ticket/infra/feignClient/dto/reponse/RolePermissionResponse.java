package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public record RolePermissionResponse(
        Resource resource,
        List<String> permissions
) {

    public record Resource(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id
    ) {}

}

