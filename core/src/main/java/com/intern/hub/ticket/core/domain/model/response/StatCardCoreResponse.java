package com.intern.hub.ticket.core.domain.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StatCardCoreResponse {

    int totalTicket;
    int totalTicketApprove;
    int totalTicketReject;
    int totalTicketPending;
}
