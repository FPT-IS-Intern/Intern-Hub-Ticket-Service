package com.intern.hub.ticket.core.domain.model.command;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public record CreateTicketCommand(
        Long userId,
        Long ticketTypeId,
        Map<String, Object> payload,
        MultipartFile[] evidences) {

}