package com.intern.hub.ticket.core.domain.model.command;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.ApprovalRule;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;

public record CreateTicketTypeCommand(
                String typeName,
                String description,
                List<TicketTemplateField> formConfig,
                ApprovalRule approvalRule,
                Boolean requireEvidence) {

}
