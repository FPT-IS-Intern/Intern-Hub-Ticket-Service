package com.intern.hub.ticket.core.domain.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RemoteRequest extends BaseAuditDomain {
    private Long ticketId;
    private Long workLocationId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String remoteType;
    private Integer version;
}
