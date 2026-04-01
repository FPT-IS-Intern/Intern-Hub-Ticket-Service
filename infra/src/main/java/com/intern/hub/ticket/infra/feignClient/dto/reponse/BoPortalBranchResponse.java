package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoPortalBranchResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isActive;
}
