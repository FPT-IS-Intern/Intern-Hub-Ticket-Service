package com.intern.hub.ticket.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.hub.ticket.infra.persistence.entity.LeaveRequestEntity;

public interface JpaLeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {
}
