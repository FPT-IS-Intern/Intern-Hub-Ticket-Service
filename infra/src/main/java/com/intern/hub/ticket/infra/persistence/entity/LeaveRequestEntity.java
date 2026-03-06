package com.intern.hub.ticket.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "leave_requests", schema = "ih_ticket")
public class LeaveRequestEntity extends BaseAuditEntity {

    @Id
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "status", length = 50)
    private String status;

    @Version
    @Column(name = "version")
    private Integer version;
}
