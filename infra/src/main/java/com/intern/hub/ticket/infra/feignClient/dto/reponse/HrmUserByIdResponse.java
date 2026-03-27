package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO nhận response từ HRM internal API: GET /hrm/internal/users/{userId}.
 * Maps trực tiếp từ UserResponse của HRM Service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HrmUserByIdResponse {
    Long userId;
    String email;
    String fullName;
}
