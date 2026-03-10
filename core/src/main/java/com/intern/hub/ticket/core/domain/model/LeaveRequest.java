package com.intern.hub.ticket.core.domain.model;

import com.intern.hub.starter.security.entity.AuditEntity;

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
public class LeaveRequest extends AuditEntity {
    private Long ticketId;
    private Long leaveTypeId;
    private Integer totalDays;
    private Integer version;
}
