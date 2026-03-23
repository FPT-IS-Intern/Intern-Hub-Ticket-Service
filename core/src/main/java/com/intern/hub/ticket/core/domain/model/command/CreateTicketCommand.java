package com.intern.hub.ticket.core.domain.model.command;

import java.util.Map;

public record CreateTicketCommand(
        Long userId,
        Long ticketTypeId,
        Map<String, Object> payload,
        FileCommand[] evidences) {

}