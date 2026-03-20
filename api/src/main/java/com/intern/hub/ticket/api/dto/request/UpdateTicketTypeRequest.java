package com.intern.hub.ticket.api.dto.request;

import java.util.List;

import com.intern.hub.ticket.api.dto.ApprovalRuleDto;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;

import jakarta.validation.constraints.Size;

public record UpdateTicketTypeRequest(
        String typeName,

        @Size(max = 500, message = "Description must be less than 500 characters") String description,

        List<TicketTemplateField> formConfig,

        ApprovalRuleDto approvalRule) {

}
