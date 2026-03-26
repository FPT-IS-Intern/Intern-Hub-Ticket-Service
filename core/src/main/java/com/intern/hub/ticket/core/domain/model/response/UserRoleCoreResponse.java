package com.intern.hub.ticket.core.domain.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleCoreResponse {
    String id;
    String name;
    String description;
    String status;
}
