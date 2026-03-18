package com.intern.hub.ticket.core.domain.model;

import com.intern.hub.starter.security.entity.AuditEntity;
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
public class TicketTypeApproverModel extends AuditEntity {
    Long ticketTypeApproverId;
    Long ticketTypeId;
    Long approverId;
}
