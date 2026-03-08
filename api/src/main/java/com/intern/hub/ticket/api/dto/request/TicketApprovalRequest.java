package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketApprovalRequest {

    @NotBlank(message = "Comment cannot be blank")
    private String comment;
}
