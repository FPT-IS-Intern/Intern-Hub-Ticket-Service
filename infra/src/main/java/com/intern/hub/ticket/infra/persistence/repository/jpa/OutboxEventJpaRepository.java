package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intern.hub.ticket.core.domain.model.enums.OutboxStatus;
import com.intern.hub.ticket.infra.persistence.entity.OutboxEvent;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OutboxEvent o SET o.status = :status, o.updatedAt = :time WHERE o.eventId IN :ids")
    void updateStatus(@Param("status") OutboxStatus status, @Param("time") Long time, @Param("ids") List<Long> ids);
}