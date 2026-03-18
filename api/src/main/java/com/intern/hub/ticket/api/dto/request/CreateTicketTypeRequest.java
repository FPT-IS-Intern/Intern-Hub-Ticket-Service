package com.intern.hub.ticket.api.dto.request;

import java.util.List;
import com.intern.hub.ticket.api.dto.ApprovalRuleDto;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;

import jakarta.validation.constraints.NotNull;

public record CreateTicketTypeRequest(
    @NotNull(message = "typeName is required") String typeName,
    String description,
    @NotNull(message = "template is required") List<TicketTemplateField> template,
    ApprovalRuleDto approvalRule
) {}