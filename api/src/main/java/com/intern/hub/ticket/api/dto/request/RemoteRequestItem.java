package com.intern.hub.ticket.api.dto.request;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteRequestItem {
    @NotNull(message = "Work Location ID is required")
    private Long workLocationId;

    @NotNull(message = "Start date is required")
    private OffsetDateTime startAt;

    @NotNull(message = "End date is required")
    private OffsetDateTime endAt;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotBlank(message = "Remote Type is required")
    private String remoteType;
}
