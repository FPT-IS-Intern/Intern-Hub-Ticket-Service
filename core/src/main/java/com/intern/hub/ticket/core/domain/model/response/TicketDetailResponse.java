package com.intern.hub.ticket.core.domain.model.response;

import com.intern.hub.ticket.core.domain.model.TicketModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDetailResponse {
    TicketModel ticketDetail;
    ApprovalInfoCoreResponse ticketApprovalInfo;

    String senderFullName;
    String fullName;
    String email;
    String approverFullNameLevel1;
    String approverFullNameLevel2;
}