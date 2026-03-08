package com.intern.hub.ticket.core.domain.command;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDto {
    private Long ticketId;
    private Long userId;
    private Long ticketTypeId;
    private String ticketTypeName;
    private LocalDate startAt;
    private LocalDate endAt;
    private String reason;
    private String status;
}
