//version 2
package com.intern.hub.ticket.infra.persistence.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.infra.persistence.entity.converter.JpaConverterJson;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
public class Ticket extends AuditEntity {

    @Id
    @Column(name = "ticket_id")
    Long ticketId;

    @Column(nullable = false)
    Long userId;

    @Column(name = "ticket_type_id", insertable = false, updatable = false)
    Long ticketTypeId;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", referencedColumnName = "ticket_type_id")
    TicketType ticketType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    TicketStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Convert(converter = JpaConverterJson.class)
    Map<String, Object> payload;

    @Column(name = "required_approvals")
    Integer requiredApprovals;

    @Builder.Default
    @Column(name = "current_approval_level")
    Integer currentApprovalLevel = 1;

    @Version
    @Column(nullable = false)
    Integer version;

    @Builder.Default
    @Column(nullable = false)
    Boolean isDeleted = false;
}