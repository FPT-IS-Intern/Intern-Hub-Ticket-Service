package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;
import com.intern.hub.ticket.core.domain.model.TicketTypeModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.enums.TicketStatus;
import com.intern.hub.ticket.core.domain.port.RuleEvaluatorPort;
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
    private final RuleEvaluatorPort ruleEvaluator;

    @Override
    @Transactional
    public TicketModel create(CreateTicketCommand command) {

        TicketTypeModel ticketType = ticketTypeRepository.findById(command.ticketTypeId())
                .orElseThrow(() -> new BadRequestException("bad.request", "Ticket type not found"));

        validatePayloadAgainstTemplate(command.payload(), ticketType.getTemplate());

        int requiredApprovals = 1;
        if (ticketType.getApprovalRule() != null) {
            boolean isTrue = ruleEvaluator.evaluate(ticketType.getApprovalRule().getCondition(), command.payload());
            requiredApprovals = isTrue ? ticketType.getApprovalRule().getLevelsIfTrue() : ticketType.getApprovalRule().getLevelsIfFalse();
        }

        TicketModel ticket = TicketModel.builder()
                .ticketId(snowflake.next())
                .userId(command.userId())
                .ticketTypeId(command.ticketTypeId())
                .status(TicketStatus.PENDING)
                .payload(command.payload() != null ? command.payload() : new HashMap<>())
                .requiredApprovals(requiredApprovals)
                .currentApprovalLevel(1)
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
        Map<String, Object> safePayload = payload != null ? payload : new HashMap<>();
        for (TicketTemplateField field : template) {
            if (field.isRequired() && !safePayload.containsKey(field.getFieldCode())) {
                throw new BadRequestException("bad.request", "Missing required field: " + field.getFieldCode());
            }
        }
    }
}