package com.intern.hub.ticket.infra.persistence.entity;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.intern.hub.starter.security.entity.AuditEntity;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;
import com.intern.hub.ticket.infra.persistence.entity.converter.JpaConverterListTicketTemplateField;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "ticket_types")
@EntityListeners(AuditingEntityListener.class)
public class TicketType extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_type_id")
    Long ticketTypeId;

    @Column(nullable = false)
    String typeName;

    String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Convert(converter = JpaConverterListTicketTemplateField.class)
    List<TicketTemplateField> template;

    @Version
    @Column(nullable = false)
    Integer version;

    @Builder.Default
    @Column(nullable = false)
    Boolean isDeleted = false;
}
