//Version 2
package com.intern.hub.ticket.core.domain.model;

import java.util.Map;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketModel extends AuditEntity {
    Long ticketId;
    Long userId;
    Long ticketTypeId;
    TicketStatus status;
    Map<String, Object> payload;
    Integer requiredApprovals;
    @Builder.Default
    Integer currentApprovalLevel = 1;
    @Setter(AccessLevel.NONE)
    Integer version;
    @Builder.Default
    Boolean isDeleted = false;
    Long approverId;
}