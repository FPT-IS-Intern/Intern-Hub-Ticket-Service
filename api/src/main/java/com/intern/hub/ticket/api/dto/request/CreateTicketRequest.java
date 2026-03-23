package com.intern.hub.ticket.api.dto.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateTicketRequest(
                @NotNull(message = "ticketTypeId is required")
                Long ticketTypeId,

                @NotNull(message = "payload is required")
                Map<String, Object> payload,

                MultipartFile[] evidences) {
}