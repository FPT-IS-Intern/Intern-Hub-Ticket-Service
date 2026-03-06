package com.intern.hub.ticket.infra.persistence.entity;

import java.time.LocalDate;

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
@Table(name = "ticket_approvals", schema = "ih_ticket")
public class TicketApprovalEntity extends BaseAuditEntity {

    @Id
    @Column(name = "approval_id")
    private Long approvalId;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "action", length = 50)
    private String action;

    @Column(name = "comment")
    private String comment;

    @Column(name = "action_at")
    private LocalDate actionAt;

    @Column(name = "status", length = 50)
    private String status;

    @Version
    @Column(name = "version")
    private Integer version;
}
