package com.intern.hub.ticket.core.domain.usecase;

import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;

public interface ApproveTicketUsecase {
    void approve(ApproveTicketCommand command);
}
