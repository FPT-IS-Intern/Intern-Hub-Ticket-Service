package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import lombok.*;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInfraResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    String id;
    String name;
    String description;
    String status;
}
