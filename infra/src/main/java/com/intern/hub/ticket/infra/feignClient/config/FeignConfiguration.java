package com.intern.hub.ticket.infra.feignClient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignConfiguration {

    @Value("${security.internal-secret}")
    private String internalSecret;

    @Bean
    public RequestInterceptor internalSecretHeaderInterceptor() {
        return requestTemplate -> {
            if (internalSecret == null) {
                return;
            }

            String normalizedSecret = internalSecret.trim();
            if (!normalizedSecret.isEmpty()) {
                requestTemplate.header("X-Internal-Secret", normalizedSecret);
            }
        };
    }
}
