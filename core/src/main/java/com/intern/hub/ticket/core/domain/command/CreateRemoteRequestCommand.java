package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateRemoteRequestCommand {
    private Long userId;
    private Long workLocationId;
    private LocalDate startAt;
    private LocalDate endAt;
    private String reason;
    private String remoteType;
}
