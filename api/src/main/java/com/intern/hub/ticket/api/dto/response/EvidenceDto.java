package com.intern.hub.ticket.api.dto.response;

import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

public record EvidenceDto(
                @JsonSerialize(using = ToStringSerializer.class) //
                Long id,

                @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

                String evidenceKey,
                String fileType,
                Long fileSize,
                EvidenceStatus status,
                Long createdAt,
                Long updatedAt,
                Long createdBy,
                Long updatedBy) {
}