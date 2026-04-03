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
    Long approvedAt;

    // ---- Transient fields: populated at query time from HRM Service (NOT persisted) ----
    String fullName;   // from HRM
    String email;      // from HRM
    String typeName;   // from TicketType (loaded at query time)
    String approverFullName; // from HRM (approver's fullName)
    String senderFullName;  // from HRM (sender's fullName, used in ticket detail)
    String approverFullNameLevel1;  // from HRM (level 1 approver fullName)
    String approverFullNameLevel2;  // from HRM (level 2 approver fullName)
    String reason;             // extracted from payload["reason"] at query time
    String statusLevel1;       // from ticket_approvals (level 1 approval status)
    String statusLevel2;       // from ticket_approvals (level 2 approval status)
}
