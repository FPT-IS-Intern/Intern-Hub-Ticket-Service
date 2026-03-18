package com.intern.hub.ticket.infra.util;

import java.util.Map;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.port.RuleEvaluatorPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpelRuleEvaluator implements RuleEvaluatorPort {

    private final ExpressionParser parser = new SpelExpressionParser();

    public boolean evaluate(String condition, Map<String, Object> payload) {
        if (condition == null || condition.isBlank()) {
            return false;
        }

        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            
            // Set payload as a variable so it can be accessed via #payload['key']
            context.setVariable("payload", payload);
            
            Boolean result = parser.parseExpression(condition).getValue(context, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Error evaluating SpEL rule: '{}'. Payload: {}", condition, payload, e);
            return false;
        }
    }
}
