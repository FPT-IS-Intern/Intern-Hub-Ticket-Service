package com.intern.hub.ticket.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTemplateField {
    private String fieldCode;
    private String fieldName;
    private String type; // TEXT, DATE, DROPDOWN...
    private boolean required;
    private List<TemplateOption> options;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TemplateOption {
        private String label;
        private String value;
    }
}
