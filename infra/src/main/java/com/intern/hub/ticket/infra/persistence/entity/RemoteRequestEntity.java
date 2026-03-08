package com.intern.hub.ticket.infra.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "remote_requests")
public class RemoteRequestEntity extends BaseAuditEntity {

    @Id
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "work_location_id")
    private Long workLocationId;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "remote_type", length = 50)
    private String remoteType;
}
