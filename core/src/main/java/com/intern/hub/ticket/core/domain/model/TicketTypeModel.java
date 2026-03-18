package com.intern.hub.ticket.core.domain.model;

import java.util.List;

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
public class TicketTypeModel extends AuditEntity{
    Long ticketTypeId;
    String typeName;
    String description;
    List<TicketTemplateField> template;
    ApprovalRule approvalRule;
    @Setter(AccessLevel.NONE)
    Integer version;
    @Builder.Default
    Boolean isDeleted = false;
}