package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RemoteRequestDto {
    private Long ticketId;
    private Long workLocationId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String remoteType;
}
