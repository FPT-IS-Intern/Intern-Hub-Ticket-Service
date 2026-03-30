package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.ForbiddenException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketApprovalModel;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveResponse;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.TicketApproveItem;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalAction;
import com.intern.hub.ticket.core.domain.model.enums.TicketApprovalStatus;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.HrmServicePort;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveTicketUsecaseImpl implements ApproveTicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketApprovalRepository ticketApprovalRepository;
    private final TicketEventPublisher ticketEventPublisher;
    private final Snowflake snowflake;
    private final TicketTaskPermissionPort permissionPort;
    private final HrmServicePort hrmServicePort;

    private ApproveTicketUsecase self;

    @Autowired
    public void setSelf(@Lazy ApproveTicketUsecase self) {
        this.self = self;
    }

    @Override
    @Transactional
    public void approve(ApproveTicketCommand command) {
        if (ticketApprovalRepository.existsByIdempotencyKey(command.idempotencyKey())) {
            throw new ConflictDataException("conflict.data", "Request has already been processed");
        }

        TicketModel ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found"));

        if (TicketStatus.APPROVED.equals(ticket.getStatus()) || TicketStatus.REJECTED.equals(ticket.getStatus())) {
            throw new BadRequestException("bad.request", "Ticket has been approved or rejected");
        }

        if (!ticket.getVersion().equals(command.version())) {
            throw new ConflictDataException("conflict.data", "The request form has been changed");
        }

        boolean isAuthorized = permissionPort.hasPermission(ticket.getTicketId(), command.approverId(),
                ticket.getCurrentApprovalLevel());
        if (!isAuthorized) {
            throw new ForbiddenException("forbidden", "You do not have the authority at this level");
        }

        TicketApprovalModel approval = TicketApprovalModel.builder()
                .approvalId(snowflake.next())
                .ticketId(ticket.getTicketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.APPROVE)
                .comment(command.comment())
                .idempotencyKey(command.idempotencyKey())
                .actionAt(System.currentTimeMillis())
                .status(TicketApprovalStatus.SUCCESS)
                .approvalLevel(ticket.getCurrentApprovalLevel())
                .build();
        ticketApprovalRepository.save(approval);

        if (ticket.getCurrentApprovalLevel() >= ticket.getRequiredApprovals()) {
            ticket.setStatus(TicketStatus.APPROVED);
            if (ticket.getTicketTypeId() == 6L) {
                callHrmProfileUpdateCallbackIfNeeded(ticket);
            }
        } else {
            ticket.setCurrentApprovalLevel(ticket.getCurrentApprovalLevel() + 1);
            ticket.setStatus(TicketStatus.REVIEWING);
        }

        ticket.setUpdatedBy(command.approverId());
        ticketRepository.save(ticket);

        ticketEventPublisher.publishTicketApprovedEvent(snowflake.next(), ticket.getTicketId(), command.approverId());
    }

    @SuppressWarnings("unchecked")
    private void callHrmProfileUpdateCallbackIfNeeded(TicketModel ticket) {
        if (ticket.getTicketTypeId() != null && ticket.getTicketTypeId() == 6L) {
            Map<String, Object> payload = ticket.getPayload();
            if (payload != null) {
                hrmServicePort.callHrmProfileApproved(ticket.getTicketId(), payload);
            }
        }
    }

    @Override
    @Transactional
    public void reject(RejectTicketCommand command) {

        // 1. Idempotency
        if (ticketApprovalRepository.existsByIdempotencyKey(command.idempotencyKey())) {
            throw new ConflictDataException("conflict.data", "Request has already been processed");
        }

        TicketModel ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found"));

        // 2. Validate quyền
        boolean isAuthorized = permissionPort.hasPermission(
                command.ticketId(),
                command.approverId(),
                ticket.getCurrentApprovalLevel()
        );

        if (!isAuthorized) {
            throw new ForbiddenException("forbidden", "No permission");
        }

        // 3. Update ticket (NO LOAD ENTITY)
        int updated = ticketRepository.rejectTicket(
                command.ticketId(),
                TicketStatus.REJECTED,
                command.approverId(),
                System.currentTimeMillis(),
                command.version()
        );

        if (updated == 0) {
            throw new ConflictDataException("conflict.data", "Ticket changed or already processed");
        }

        // 4. Insert approval log
        TicketApprovalModel approval = TicketApprovalModel.builder()
                .approvalId(snowflake.next())
                .ticketId(command.ticketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.REJECT)
                .comment(command.comment())
                .idempotencyKey(command.idempotencyKey())
                .actionAt(System.currentTimeMillis())
                .status(TicketApprovalStatus.SUCCESS)
                .build();

        ticketApprovalRepository.save(approval);

        // 5. publish event
        ticketEventPublisher.publishTicketApprovedEvent(snowflake.next(), ticket.getTicketId(), command.approverId());
    }

    @Override
    public BulkApproveResponse bulkApprove(BulkApproveTicketCommand command) {
        int successCount = 0;
        List<BulkApproveResponse.TicketErrorDetail> failedTickets = new ArrayList<>();

        for (TicketApproveItem item : command.tickets()) {
            try {

                String subIdempotencyKey = command.idempotencyKey() + ":" + item.ticketId();
                ApproveTicketCommand singleCommand = new ApproveTicketCommand(
                        item.ticketId(),
                        command.approverId(),
                        command.comment(),
                        subIdempotencyKey,
                        item.version());

                self.approve(singleCommand);
                successCount++;

            } catch (Exception e) {
                failedTickets.add(new BulkApproveResponse.TicketErrorDetail(item.ticketId(), e.getMessage()));
            }
        }
        return new BulkApproveResponse(command.tickets().size(), successCount, failedTickets);
    }
}