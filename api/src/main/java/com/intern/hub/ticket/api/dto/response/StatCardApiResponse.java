package com.intern.hub.ticket.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatCardApiResponse {
    int totalTicket;
    int totalTicketApprove;
    int totalTicketReject;
    int totalTicketPending;
}
