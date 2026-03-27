package com.intern.hub.ticket.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;

import lombok.Builder;
import lombok.Data;

/**
 * Response DTO dành riêng cho trang Quản lý phiếu yêu cầu (admin).
 * Kế thừa toàn bộ trường của TicketDto, bổ sung thêm:
 * - fullName, email: lấy từ HRM Service
 * - typeName: lấy từ TicketType
 *
 * Frontend không cần gọi thêm API HRM để resolve user info.
 */
@Data
@Builder
public class TicketManagementDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long ticketId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String fullName;   // từ HRM
    private String email;      // từ HRM

    @JsonSerialize(using = ToStringSerializer.class)
    private Long ticketTypeId;

    private String typeName;   // từ TicketType

    private TicketStatus status;

    private Long createdAt;
    private Long updatedAt;
    private Long createdBy;
    private Long updatedBy;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long approverId;

    private String approverFullName; // từ HRM
}
