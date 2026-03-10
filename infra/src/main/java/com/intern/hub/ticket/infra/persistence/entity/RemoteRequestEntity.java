package com.intern.hub.ticket.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "remote_requests", schema = "ih_ticket")
public class RemoteRequestEntity extends AuditEntity {

    @Id
    @Column(name = "ticket_id")
    private Long ticketId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @Column(name = "work_location_id")
    private Long workLocationId;

    @Column(name = "remote_type", length = 50)
    private String remoteType;

    @Version
    @Column(name = "version")
    private Integer version;
}
