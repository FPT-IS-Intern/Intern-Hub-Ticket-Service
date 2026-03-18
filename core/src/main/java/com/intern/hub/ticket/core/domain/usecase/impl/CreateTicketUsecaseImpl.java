package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;
import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTicketUsecaseImpl implements CreateTicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketEventPublisher ticketEventPublisher;
    private final Snowflake snowflake;

    @Override
    public TicketModel create(CreateTicketCommand command) {

        TicketTypeModel ticketType = ticketTypeRepository.findById(command.ticketTypeId())
                .orElseThrow(() -> new BadRequestException("bad.request", "Ticket type not found"));

        validatePayloadAgainstTemplate(command.payload(), ticketType.getTemplate());

        TicketModel ticket = TicketModel.builder()
                .ticketId(snowflake.next())
                .userId(command.userId())
                .ticketTypeId(command.ticketTypeId())
                .status(TicketStatus.PENDING)
                .payload(command.payload() != null ? command.payload() : new HashMap<>())
                .isDeleted(false)
                .build();

        TicketModel savedTicket = ticketRepository.save(ticket);

        ticketEventPublisher.publishTicketCreatedEvent(
                snowflake.next(),
                savedTicket.getTicketId(),
                savedTicket.getUserId(),
                savedTicket.getTicketTypeId());

        return savedTicket;
    }

    private void validatePayloadAgainstTemplate(Map<String, Object> payload, List<TicketTemplateField> template) {
        if (template == null || template.isEmpty()) {
            return;
        }

        Map<String, Object> safePayload = (payload != null) ? payload : Map.of();
        List<String> missingFields = new ArrayList<>();

        for (TicketTemplateField fieldDef : template) {
            String fieldCode = fieldDef.getFieldCode();
            boolean required = fieldDef.isRequired();

            if (fieldCode != null && required) {
                Object value = safePayload.get(fieldCode);
                if (value == null || (value instanceof String && ((String) value).isBlank())) {
                    missingFields.add(fieldCode);
                }
            }
        }

        if (!missingFields.isEmpty()) {
            throw new BadRequestException("bad.request",
                    "Missing required fields: " + String.join(", ", missingFields));
        }
    }
}