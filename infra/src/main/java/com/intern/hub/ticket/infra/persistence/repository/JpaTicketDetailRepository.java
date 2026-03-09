package com.intern.hub.ticket.infra.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.hub.ticket.infra.persistence.entity.TicketEntity;

@Repository
public interface JpaTicketDetailRepository extends JpaRepository<TicketEntity, Long> {

    @Query("SELECT t FROM TicketEntity t LEFT JOIN FETCH t.ticketType WHERE t.ticketId = :ticketId")
    Optional<TicketEntity> findTicketWithRelationById(@Param("ticketId") Long ticketId);
}
