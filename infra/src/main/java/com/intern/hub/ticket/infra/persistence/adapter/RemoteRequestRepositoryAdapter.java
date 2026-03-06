package com.intern.hub.ticket.infra.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.core.domain.model.RemoteRequest;
import com.intern.hub.ticket.core.port.repository.RemoteRequestRepository;
import com.intern.hub.ticket.infra.persistence.mapper.RemoteRequestMapper;
import com.intern.hub.ticket.infra.persistence.repository.JpaRemoteRequestRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RemoteRequestRepositoryAdapter implements RemoteRequestRepository {

    private final JpaRemoteRequestRepository jpaRepository;
    private final RemoteRequestMapper mapper;

    @Override
    public Optional<RemoteRequest> findByTicketId(Long ticketId) {
        return jpaRepository.findById(ticketId).map(mapper::toDomain);
    }

    @Override
    public RemoteRequest save(RemoteRequest remoteRequest) {
        var entity = mapper.toEntity(remoteRequest);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteByTicketId(Long ticketId) {
        jpaRepository.deleteById(ticketId);
    }
}
