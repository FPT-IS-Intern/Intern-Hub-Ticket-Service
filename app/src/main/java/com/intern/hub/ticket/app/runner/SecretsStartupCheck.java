package com.intern.hub.ticket.app.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SecretsStartupCheck implements ApplicationRunner {
    @Value("${security.internal-secret:}")
    private String internalSecret;

    @Override
    public void run(ApplicationArguments args) {
        if (internalSecret == null || internalSecret.isBlank()) {
            throw new IllegalStateException("INTERNAL_SECRET_KEY is not set");
        }
    }
}
