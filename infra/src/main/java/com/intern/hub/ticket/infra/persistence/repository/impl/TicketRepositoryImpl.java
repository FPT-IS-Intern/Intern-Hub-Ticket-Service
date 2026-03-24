package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.infra.mapper.TicketMapper;
import com.intern.hub.ticket.infra.persistence.entity.Ticket;
import com.intern.hub.ticket.infra.persistence.entity.TicketType;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketJpaRepository;
import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketTypeJpaRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository jpaRepository;
    private final TicketTypeJpaRepository ticketTypeJpaRepository;
    private final TicketMapper mapper;

    @Override
    public TicketModel save(TicketModel model) {
        Ticket entity = mapper.toEntity(model);
        Ticket savedEntity = jpaRepository.save(entity);

        if (savedEntity.getTicketTypeId() != null) {
            ticketTypeJpaRepository.findById(savedEntity.getTicketTypeId())
                    .ifPresent(savedEntity::setTicketType);
        }

        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<TicketModel> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<TicketModel> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<TicketModel> findByStatus(TicketStatus status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toModel).toList();
    }

    @Override
    public PaginatedData<TicketModel> findAllPaginated(int page, int size) {
        Page<Ticket> ticketPage = jpaRepository.findAll(PageRequest.of(page, size));
        return mapper.toPaginatedModel(ticketPage);
    }

    @Override
    public PaginatedData<TicketModel> findAllPaginated(int page, int size, List<Long> userIds, String typeName, String status) {
        Specification<Ticket> spec = (root, query, cb) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();

            if (status != null && !status.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("status"), TicketStatus.valueOf(status.toUpperCase())));
                } catch (IllegalArgumentException ignored) {
                }
            }

            if (typeName != null && !typeName.isBlank()) {
                Join<Ticket, TicketType> typeJoin = root.join("ticketType");
                predicates.add(cb.like(cb.lower(typeJoin.get("typeName")), "%" + typeName.toLowerCase() + "%"));
            }

            // userId filter
            if (userIds != null && !userIds.isEmpty()) {
                predicates.add(root.get("userId").in(userIds));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Ticket> ticketPage = jpaRepository.findAll(spec, PageRequest.of(page, size));
        return mapper.toPaginatedModel(ticketPage);
    }

    @Override
    public Collection<TicketModel> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream().map(mapper::toModel).toList();
    }

}