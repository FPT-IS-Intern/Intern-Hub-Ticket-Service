package com.intern.hub.ticket.infra.model.ressponse;

import lombok.*;

public interface ApprovalDetailInfoProjection {

    Long getTicketId();
    Long getUserId();
    Long getCreatedAt();

    Long getApproverIdLevel1();
    Long getApprovedAt();
    String getStatusLevel1();

    Long getApproverIdLevel2();
    Long getApprovedAtLevel2();
    String getStatusLevel2();
}