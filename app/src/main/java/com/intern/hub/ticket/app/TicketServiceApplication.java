package com.intern.hub.ticket.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;

@SpringBootApplication(scanBasePackages = "com.intern.hub.ticket", exclude = {
        UserDetailsServiceAutoConfiguration.class })
@EnableGlobalExceptionHandler
@EnableJpaRepositories(basePackages = "com.intern.hub.ticket.infra.persistence.repository")
@EntityScan(basePackages = "com.intern.hub.ticket.infra.persistence.entity")
public class TicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }
}
