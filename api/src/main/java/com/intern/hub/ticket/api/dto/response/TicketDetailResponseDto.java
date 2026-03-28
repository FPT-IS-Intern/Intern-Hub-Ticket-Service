package com.intern.hub.ticket.api.dto.response;

public record TicketDetailResponseDto(
                TicketDetailDto ticketDetail,
                TicketApprovalInfoDto ticketApprovalInfo
        ) {
}
