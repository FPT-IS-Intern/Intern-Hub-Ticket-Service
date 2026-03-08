package com.intern.hub.ticket.infra.persistence.entity;

import com.intern.hub.ticket.core.domain.model.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "leave_requests")
public class LeaveRequestEntity extends BaseAuditEntity {

    @Id
    @Column(name = "ticket_id")
    private Long ticketId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "total_days")
    private Integer totalDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private TicketStatus status;

    @Version
    @Column(name = "version")
    private Integer version;
}
