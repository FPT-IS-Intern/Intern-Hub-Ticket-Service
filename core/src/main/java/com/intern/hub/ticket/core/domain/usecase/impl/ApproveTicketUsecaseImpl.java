package com.intern.hub.ticket.core.domain.usecase.impl;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveTicketUsecaseImpl implements ApproveTicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final TicketEventPublisher ticketEventPublisher;
    private final Snowflake snowflake;

    @Override
    public void approve(ApproveTicketCommand command) {

        // Kiểm tra tính lũy đẳng (Idempotency) để chống spam
        if (ticketApprovalRepository.existsByIdempotencyKey(command.idempotencyKey())) {
            throw new ConflictDataException("ticket.already_processed", "Request has already been processed");
        }

        TicketModel ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("ticket.not_found", "Ticket not found"));

        // Validation nghiệp vụ
        if (!TicketStatus.PENDING.equals(ticket.getStatus())) {
            throw new BadRequestException("ticket.invalid_status", "Only PENDING tickets can be approved");
        }

        // Cập nhật trạng thái phiếu chính
        ticket.setStatus(TicketStatus.APPROVED);
        ticketRepository.save(ticket);

        // Tạo và lưu log lịch sử duyệt
        TicketApprovalModel approval = TicketApprovalModel.builder()
                .approvalId(snowflake.next())
                .ticketId(ticket.getTicketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.APPROVE)
                .comment(command.comment())
                .idempotencyKey(command.idempotencyKey())
                .actionAt(System.currentTimeMillis())
                .status(TicketApprovalStatus.SUCCESS)
                .build();
        ticketApprovalRepository.save(approval);

        // Bắn sự kiện ra ngoài thông qua Port
        ticketEventPublisher.publishTicketApprovedEvent(snowflake.next(), ticket.getTicketId(), command.approverId());
    }
}
