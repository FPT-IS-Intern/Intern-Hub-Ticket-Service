package com.intern.hub.ticket.api.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${openapi.server.description:Local Server}")
    private String serverDescription;

    @Bean
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            Server customServer = new Server();
            customServer.setUrl(serverUrl);
            customServer.setDescription(serverDescription);

            openApi.addServersItem(customServer);
        };
    }
}