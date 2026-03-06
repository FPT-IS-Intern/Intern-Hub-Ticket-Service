package com.intern.hub.ticket.core.port.repository;

import java.util.Optional;

import com.intern.hub.ticket.core.domain.model.RemoteRequest;

public interface RemoteRequestRepository {
    Optional<RemoteRequest> findByTicketId(Long ticketId);

    RemoteRequest save(RemoteRequest remoteRequest);

    void deleteByTicketId(Long ticketId);
}
