package com.intern.hub.ticket.core.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.ticket.core.domain.model.TicketTemplateField;

public class TicketTemplateValidator {
    public void validateFieldType(TicketTemplateField field, Object value) {
        String fieldCode = field.getFieldCode();
        TemplateFieldType type = TemplateFieldType.from(field.getType());

        if (type == null) {
            return;
        }

        switch (type) {
            case TEXT -> requireString(fieldCode, value, "text");
            case DATE -> validateDate(fieldCode, value);
            case DROPDOWN -> validateDropdown(field, value);
            case NUMBER -> validateNumber(fieldCode, value);
            case BOOLEAN -> validateBoolean(fieldCode, value);
            case CHECKBOX -> validateCheckbox(field, value);
            default -> throw new BadRequestException(
                    "invalid.type",
                    "Unsupported field type for " + fieldCode + ".");
        }
    }

    private void requireString(String fieldCode, Object value, String label) {
        if (!(value instanceof String)) {
            throw new BadRequestException(
                    "invalid.type",
                    "Field " + fieldCode + " must be " + label + " (String).");
        }
    }

    private void validateDate(String fieldCode, Object value) {
        if (!(value instanceof String str)) {
            throw new BadRequestException(
                    "invalid.type",
                    "Field " + fieldCode + " must be a date string.");
        }

        try {
            LocalDate.parse(str.trim());
        } catch (DateTimeParseException e) {
            throw new BadRequestException(
                    "invalid.format",
                    "Field " + fieldCode + " must be in yyyy-MM-dd format.");
        }
    }

    private void validateDropdown(TicketTemplateField field, Object value) {
        String fieldCode = field.getFieldCode();

        if (!(value instanceof String str)) {
            throw new BadRequestException(
                    "invalid.type",
                    "Field " + fieldCode + " must be a text value.");
        }

        // Location options are dynamic and fetched from BO Portal branches at runtime.
        // Keep type validation only, skip static option constraint from form_config.
        if ("location".equalsIgnoreCase(fieldCode)) {
            return;
        }

        Set<String> validOptions = optionValueSet(field);
        if (!validOptions.contains(str)) {
            throw new BadRequestException(
                    "invalid.value",
                    "Invalid option for field " + fieldCode + ". Value '" + str + "' is not allowed.");
        }
    }

    private void validateNumber(String fieldCode, Object value) {
        if (value instanceof Number) {
            return;
        }

        if (value instanceof String str) {
            try {
                new BigDecimal(str.trim());
                return;
            } catch (NumberFormatException e) {
                throw new BadRequestException(
                        "invalid.type",
                        "Field " + fieldCode + " must be a valid number.");
            }
        }

        throw new BadRequestException(
                "invalid.type",
                "Field " + fieldCode + " must be a number.");
    }

    private void validateBoolean(String fieldCode, Object value) {
        if (value instanceof Boolean) {
            return;
        }

        if (value instanceof String str) {
            String normalized = str.trim().toLowerCase(Locale.ROOT);
            if (normalized.equals("true") || normalized.equals("false")) {
                return;
            }

            throw new BadRequestException(
                    "invalid.type",
                    "Field " + fieldCode + " must be boolean (true/false).");
        }

        throw new BadRequestException(
                "invalid.type",
                "Field " + fieldCode + " must be a boolean.");
    }

    private void validateCheckbox(TicketTemplateField field, Object value) {
        String fieldCode = field.getFieldCode();

        if (!(value instanceof List<?> selectedValues)) {
            throw new BadRequestException(
                    "invalid.type",
                    "Field " + fieldCode + " must be a list of choices (Array).");
        }

        Set<String> validOptions = optionValueSet(field);
        if (validOptions.isEmpty()) {
            return;
        }

        for (Object selected : selectedValues) {
            if (!(selected instanceof String str) || !validOptions.contains(str)) {
                throw new BadRequestException(
                        "invalid.value",
                        "Invalid option '" + selected + "' for field " + fieldCode + ".");
            }
        }
    }

    private Set<String> optionValueSet(TicketTemplateField field) {
        if (field.getOptions() == null || field.getOptions().isEmpty()) {
            return Collections.emptySet();
        }

        return field.getOptions().stream()
                .map(TicketTemplateField.TemplateOption::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private enum TemplateFieldType {
        TEXT, DATE, DROPDOWN, NUMBER, BOOLEAN, CHECKBOX;

        static TemplateFieldType from(String rawType) {
            if (rawType == null || rawType.isBlank()) {
                return null;
            }

            try {
                return TemplateFieldType.valueOf(rawType.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
