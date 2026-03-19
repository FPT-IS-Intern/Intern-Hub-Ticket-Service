package com.intern.hub.ticket.core.domain.usecase;

import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveResponse;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;

public interface ApproveTicketUsecase {
    void approve(ApproveTicketCommand command);

    void reject(RejectTicketCommand command);

    BulkApproveResponse bulkApprove(BulkApproveTicketCommand command);
}
