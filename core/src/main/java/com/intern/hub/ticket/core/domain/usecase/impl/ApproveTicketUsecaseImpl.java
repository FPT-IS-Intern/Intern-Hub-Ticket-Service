package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void approve(ApproveTicketCommand command) {
        if (ticketApprovalRepository.existsByIdempotencyKey(command.idempotencyKey())) {
            return;
        }

        TicketModel ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found"));

        if (TicketStatus.APPROVED.equals(ticket.getStatus()) || TicketStatus.REJECTED.equals(ticket.getStatus())) {
            return;
        }

        if (!ticket.getVersion().equals(command.version())) {
            throw new ConflictDataException("conflict.data", "The request form has been changed");
        }

        int currentApprovalLevel = normalizeApprovalLevel(ticket.getCurrentApprovalLevel());
        int requiredApprovals = normalizeApprovalLevel(ticket.getRequiredApprovals());
        boolean hasLevel2Permission = permissionPort.hasPermission(
                ticket.getTicketId(),
                ticket.getTicketTypeId(),
                command.approverId(),
                2);

        boolean isAuthorized = permissionPort.hasPermission(
                ticket.getTicketId(),
                ticket.getTicketTypeId(),
                command.approverId(),
                currentApprovalLevel);
        if (!isAuthorized) {
            throw new ForbiddenException("forbidden", "No permission");
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
                .approvalLevel(currentApprovalLevel)
                .build();
        ticketApprovalRepository.save(approval);

        boolean canAutoFinalizeAsLevel2 = requiredApprovals == 2
                && currentApprovalLevel == 1
                && hasLevel2Permission;

        if (canAutoFinalizeAsLevel2) {
            TicketApprovalModel level2Approval = TicketApprovalModel.builder()
                    .approvalId(snowflake.next())
                    .ticketId(ticket.getTicketId())
                    .approverId(command.approverId())
                    .action(TicketApprovalAction.APPROVE)
                    .comment(command.comment())
                    .idempotencyKey(command.idempotencyKey() + ":L2")
                    .actionAt(System.currentTimeMillis())
                    .status(TicketApprovalStatus.SUCCESS)
                    .approvalLevel(2)
                    .build();
            ticketApprovalRepository.save(level2Approval);
        }

        if (canAutoFinalizeAsLevel2) {
            ticket.setCurrentApprovalLevel(2);
            ticket.setStatus(TicketStatus.APPROVED);
            if (ticket.getTicketTypeId() != null && ticket.getTicketTypeId() == 6L) {
                callHrmProfileUpdateCallbackIfNeeded(ticket);
            }
        } else if (currentApprovalLevel < requiredApprovals) {
            ticket.setCurrentApprovalLevel(currentApprovalLevel + 1);
            ticket.setStatus(TicketStatus.REVIEWING);
        } else {
            ticket.setStatus(TicketStatus.APPROVED);
            if (ticket.getTicketTypeId() != null && ticket.getTicketTypeId() == 6L) {
                callHrmProfileUpdateCallbackIfNeeded(ticket);
            }
        }

        ticket.setUpdatedBy(command.approverId());
        ticketRepository.save(ticket);

        if (TicketStatus.APPROVED.equals(ticket.getStatus())) {
            ticketEventPublisher.publishTicketApprovedEvent(
                    snowflake.next(),
                    ticket.getTicketId(),
                    command.approverId());
        }
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
        if (ticketApprovalRepository.existsByIdempotencyKey(command.idempotencyKey())) {
            throw new ConflictDataException("conflict.data", "Request has already been processed");
        }

        TicketModel ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found"));

        int currentApprovalLevel = normalizeApprovalLevel(ticket.getCurrentApprovalLevel());
        boolean isAuthorized = permissionPort.hasPermission(
                ticket.getTicketId(),
                ticket.getTicketTypeId(),
                command.approverId(),
                currentApprovalLevel);
        if (!isAuthorized) {
            throw new ForbiddenException("forbidden", "No permission");
        }

        int updated = ticketRepository.rejectTicket(
                command.ticketId(),
                TicketStatus.REJECTED,
                command.approverId(),
                System.currentTimeMillis(),
                command.version());
        if (updated == 0) {
            throw new ConflictDataException("conflict.data", "Ticket changed or already processed");
        }

        TicketApprovalModel approval = TicketApprovalModel.builder()
                .approvalId(snowflake.next())
                .ticketId(command.ticketId())
                .approverId(command.approverId())
                .action(TicketApprovalAction.REJECT)
                .comment(command.comment())
                .idempotencyKey(command.idempotencyKey())
                .actionAt(System.currentTimeMillis())
                .status(TicketApprovalStatus.SUCCESS)
                .approvalLevel(currentApprovalLevel)
                .build();
        ticketApprovalRepository.save(approval);

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

    private int normalizeApprovalLevel(Integer level) {
        if (level == null || level <= 1) {
            return 1;
        }
        return 2;
    }
}
