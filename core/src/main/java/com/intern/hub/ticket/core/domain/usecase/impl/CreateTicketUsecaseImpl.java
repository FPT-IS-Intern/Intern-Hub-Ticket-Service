package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            requiredApprovals = isTrue ? ticketType.getApprovalRule().getLevelsIfTrue()
                    : ticketType.getApprovalRule().getLevelsIfFalse();
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
            if (payload != null && !payload.isEmpty()) {
                throw new BadRequestException("bad.request", "Template is empty but payload contains data.");
            }
            return;
        }

        Map<String, Object> safePayload = payload != null ? payload : new HashMap<>();

        Set<String> validFieldCodes = template.stream()
                .map(TicketTemplateField::getFieldCode)
                .collect(Collectors.toSet());

        for (String payloadKey : safePayload.keySet()) {
            if (!validFieldCodes.contains(payloadKey)) {
                throw new BadRequestException("invalid.field", "Payload contains unknown field: " + payloadKey);
            }
        }

        for (TicketTemplateField field : template) {
            String fieldCode = field.getFieldCode();
            Object value = safePayload.get(fieldCode);

            if (field.isRequired() && isNullOrEmpty(value)) {
                throw new BadRequestException("bad.request", "Missing or empty required field: " + fieldCode);
            }

            if (!isNullOrEmpty(value)) {
                validateFieldType(field, value);
            }
        }
    }

    private boolean isNullOrEmpty(Object value) {
        return value == null || (value instanceof String && ((String) value).trim().isEmpty());
    }

    private void validateFieldType(TicketTemplateField field, Object value) {
        String type = field.getType();
        if (type == null) {
            return;
        }

        String fieldCode = field.getFieldCode();

        switch (type.toUpperCase()) {
            case "TEXT":
                if (!(value instanceof String)) {
                    throw new BadRequestException("invalid.type", "Field " + fieldCode + " must be text (String).");
                }
                break;

            case "DATE":
                if (!(value instanceof String)) {
                    throw new BadRequestException("invalid.type", "Field " + fieldCode + " must be a date string.");
                }
                // TODO: Bạn có thể thêm logic dùng LocalDate.parse() để check xem format có
                // đúng không (VD: yyyy-MM-dd)
                /*
                 * Ví dụ:
                 * try {
                 * LocalDate.parse((String) value);
                 * } catch (DateTimeParseException e) {
                 * throw new BadRequestException("invalid.format", "Field " + fieldCode +
                 * " must be in yyyy-MM-dd format.");
                 * }
                 */
                break;

            case "DROPDOWN":
                if (!(value instanceof String)) {
                    throw new BadRequestException("invalid.type", "Field " + fieldCode + " must be a text value.");
                }

                String stringValue = (String) value;
                boolean isValidOption = false;

                if (field.getOptions() != null) {
                    isValidOption = field.getOptions().stream()
                            .anyMatch(option -> stringValue.equals(option.getValue()));
                }

                if (!isValidOption) {
                    throw new BadRequestException("invalid.value",
                            "Invalid option for field " + fieldCode + ". Value '" + stringValue + "' is not allowed.");
                }
                break;

            case "NUMBER":
                if (value instanceof Number) {
                    break;
                } else if (value instanceof String) {
                    try {
                        new java.math.BigDecimal((String) value);
                    } catch (NumberFormatException e) {
                        throw new BadRequestException("invalid.type",
                                "Field " + fieldCode + " must be a valid number.");
                    }
                } else {
                    throw new BadRequestException("invalid.type", "Field " + fieldCode + " must be a number.");
                }
                break;

            case "BOOLEAN":
                if (value instanceof Boolean) {
                    break;
                } else if (value instanceof String) {
                    String strBool = ((String) value).toLowerCase().trim();
                    if (!strBool.equals("true") && !strBool.equals("false")) {
                        throw new BadRequestException("invalid.type",
                                "Field " + fieldCode + " must be boolean (true/false).");
                    }
                } else {
                    throw new BadRequestException("invalid.type", "Field " + fieldCode + " must be a boolean.");
                }
                break;

            case "CHECKBOX":

                if (!(value instanceof java.util.List)) {
                    throw new BadRequestException("invalid.type",
                            "Field " + fieldCode + " must be a list of choices (Array).");
                }

                java.util.List<?> selectedValues = (java.util.List<?>) value;

                if (field.getOptions() != null && !field.getOptions().isEmpty()) {
                    java.util.Set<String> validOptions = field.getOptions().stream()
                            .map(TicketTemplateField.TemplateOption::getValue)
                            .collect(java.util.stream.Collectors.toSet());

                    for (Object selected : selectedValues) {
                        if (!(selected instanceof String) || !validOptions.contains((String) selected)) {
                            throw new BadRequestException("invalid.value",
                                    "Invalid option '" + selected + "' for field " + fieldCode + ".");
                        }
                    }
                }
                break;

            // Thêm các case khác như NUMBER, BOOLEAN, CHECKBOX... ở đây
            default:
                // Log cảnh báo nếu có type mới mà chưa được support
                break;
        }
    }
}