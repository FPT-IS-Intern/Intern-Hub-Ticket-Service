package com.intern.hub.ticket.core.domain.model;

import com.intern.hub.starter.security.entity.AuditModel;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;

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
public class EvidenceModel extends AuditModel {
    Long evidenceId;
    Long ticketId;
    String evidenceKey;
    String fileType;
    Long fileSize;
    EvidenceStatus status;
    @Setter(AccessLevel.NONE)
    Integer version;
}
