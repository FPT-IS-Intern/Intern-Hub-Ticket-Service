package com.intern.hub.ticket.infra.adapter;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketTaskPermissionAdapter implements TicketTaskPermissionPort {

    @Override
    public boolean hasPermission(Long ticketId, Long approverId, int currentLevel) {
        // TODO: Implement actual multi-level permission logic
        return true;
    }
}
