package com.intern.hub.ticket.core.domain.model.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApprovalInfoCoreResponse {
    Long ticketId;

    String senderFullName;
    Long userId;
    Long createdAt;

    Long approverIdLevel1;
    Long approvedAt;
    String statusLevel1;
    String approverFullNameLevel1;

    Long approverIdLevel2;
    Long approvedAtLevel2;
    String statusLevel2;
    String approverFullNameLevel2;
}
