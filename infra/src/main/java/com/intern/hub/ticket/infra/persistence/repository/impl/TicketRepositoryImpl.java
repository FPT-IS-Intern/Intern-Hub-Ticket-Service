package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

        if (savedEntity.getTicketType().getTicketTypeId() != null) {
            ticketTypeJpaRepository.findById(savedEntity.getTicketType().getTicketTypeId())
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
        Specification<Ticket> spec = buildFilterSpecification(userIds, typeName, status, null, null);

        Page<Ticket> ticketPage = jpaRepository.findAll(spec, PageRequest.of(page, size));
        return mapper.toPaginatedModel(ticketPage);
    }

    @Override
    public PaginatedData<TicketModel> findAllPaginated(
            int page, int size, List<Long> userIds, String typeName, String status,
            Long startDate, Long endDate, String sortBy, String sortDirection) {
        Specification<Ticket> spec = buildFilterSpecification(userIds, typeName, status, startDate, endDate);

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = resolveSortField(sortBy);
        Sort sort = Sort.by(direction, sortField);

        Page<Ticket> ticketPage = jpaRepository.findAll(spec, PageRequest.of(page, size, sort));
        return mapper.toPaginatedModel(ticketPage);
    }

    private Specification<Ticket> buildFilterSpecification(List<Long> userIds, String typeName, String status, Long startDate, Long endDate) {
        return (root, query, cb) -> {
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

            if (userIds != null && !userIds.isEmpty()) {
                predicates.add(root.get("userId").in(userIds));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String resolveSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }
        return switch (sortBy.toLowerCase()) {
            case "updatedat" -> "updatedAt";
            case "status" -> "status";
            case "createdat" -> "createdAt";
            case "typename" -> "ticketType.typeName";
            default -> "createdAt";
        };
    }

    @Override
    public Collection<TicketModel> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream().map(mapper::toModel).toList();
    }

    @Override
    public int rejectTicket(Long ticketId, TicketStatus status, Long updatedBy, Long updatedAt, Integer version) {
        return jpaRepository.rejectTicket(ticketId, status, updatedBy, updatedAt, version);
    }

}