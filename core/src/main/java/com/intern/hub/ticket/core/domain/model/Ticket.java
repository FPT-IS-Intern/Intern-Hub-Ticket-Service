package com.intern.hub.ticket.core.domain.model;

import java.time.OffsetDateTime;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ticket extends AuditEntity {
    private Long ticketId;
    private Long userId;
    private Long ticketTypeId;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private String reason;
    private TicketStatus status;
    private Integer version;
    private boolean isDeleted;
}
