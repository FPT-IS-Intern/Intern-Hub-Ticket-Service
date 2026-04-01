package com.intern.hub.ticket.infra.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intern.hub.ticket.infra.persistence.entity.TicketGlobalApprover;

public interface TicketGlobalApproverJpaRepository extends JpaRepository<TicketGlobalApprover, Long> {

    @Query("select t.approverId from TicketGlobalApprover t where t.maxApprovalLevel >= :minLevel")
    List<Long> findApproverIdsByMinLevel(@Param("minLevel") int minLevel);
}

