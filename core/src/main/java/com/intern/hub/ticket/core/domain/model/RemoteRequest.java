package com.intern.hub.ticket.core.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RemoteRequest extends BaseAuditDomain {
    private Long ticketId;
    private Long workLocationId;
    private String remoteType;
    private Integer version;
}
