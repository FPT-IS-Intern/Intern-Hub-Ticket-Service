package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInfraResponse {
    String id;
    String name;
    String description;
    String status;
}
