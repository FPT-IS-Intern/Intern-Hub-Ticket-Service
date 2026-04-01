package com.intern.hub.ticket.api.dto.response;

import java.util.UUID;

public record BranchDto(
        UUID id,
        String name,
        String description,
        Boolean isActive
) {}
