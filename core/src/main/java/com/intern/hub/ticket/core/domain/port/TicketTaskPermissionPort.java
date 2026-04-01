package com.intern.hub.ticket.core.domain.port;

public interface TicketTaskPermissionPort {
    /**
     * Kiểm tra xem approverId có được phép duyệt ticket ở currentLevel hiện tại hay không.
     */
    boolean hasPermission(Long ticketId, Long ticketTypeId, Long approverId, int currentLevel);
}
