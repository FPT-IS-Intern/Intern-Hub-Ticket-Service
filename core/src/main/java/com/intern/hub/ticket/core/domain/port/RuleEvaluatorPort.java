package com.intern.hub.ticket.core.domain.port;

import java.util.Map;

public interface RuleEvaluatorPort {
    boolean evaluate(String condition, Map<String, Object> payload);
}
