package com.intern.hub.ticket.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.starter.security.annotation.EnableSecurity;

@SpringBootApplication(scanBasePackages = { "com.intern.hub.ticket.app",
        "com.intern.hub.ticket.core",
        "com.intern.hub.ticket.infra",
        "com.intern.hub.ticket.api" }, exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableSecurity
@EnableGlobalExceptionHandler
@EnableJpaRepositories(basePackages = "com.intern.hub.ticket.infra.persistence.repository")
@EntityScan(basePackages = "com.intern.hub.ticket.infra.persistence.entity")
@EnableScheduling
@EnableFeignClients(basePackages = "com.intern.hub.ticket.infra.feignClient")
public class TicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }
}
