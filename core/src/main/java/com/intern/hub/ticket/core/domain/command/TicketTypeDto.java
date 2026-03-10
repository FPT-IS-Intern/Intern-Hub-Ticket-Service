package com.intern.hub.ticket.core.domain.command;

import lombok.Builder;

@Builder
public record TicketTypeDto(
                Long id,
                String name,
                String description) {
}
