package com.intern.hub.ticket.infra.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;


import com.intern.hub.ticket.core.domain.model.TicketTypeModel;


import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;

import com.intern.hub.ticket.infra.mapper.TicketTypeMapper;

import com.intern.hub.ticket.infra.persistence.entity.TicketType;

import com.intern.hub.ticket.infra.persistence.repository.jpa.TicketTypeJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketTypeRepositoryImpl implements TicketTypeRepository {

    private final TicketTypeJpaRepository jpaRepository;
    private final TicketTypeMapper mapper;
    @Override
    public TicketTypeModel save(TicketTypeModel model) {
        TicketType savedEntity = jpaRepository.save(mapper.toEntity(model));
        return mapper.toModel(savedEntity);
    }
    @Override
    public boolean existsByNameAndIsDeletedFalse(String name) {
        return jpaRepository.existsByTypeNameAndIsDeletedFalse(name);
    }
    @Override
    public Optional<TicketTypeModel> findById(Long id) {
       return jpaRepository.findById(id)
                .map(mapper::toModel);
    }
    @Override
    public List<TicketTypeModel> findAll() {
       List<TicketType> entities = jpaRepository.findAll();
        return mapper.toModels(entities);
    }
    
    @Override
    public List<TicketTypeModel> findAllActive() {
        return mapper.toModels(jpaRepository.findAllByIsDeletedFalse());
    }

   
}