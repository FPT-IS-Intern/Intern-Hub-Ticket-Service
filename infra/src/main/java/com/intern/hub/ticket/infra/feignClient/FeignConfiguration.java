package com.intern.hub.ticket.infra.feignClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
@EnableFeignClients(basePackages = "com.intern.hub.ticket.infra.feignClient")
public class FeignConfiguration {

    @Value("${security.internal-secret}")
    private String secretKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Internal-Secret", secretKey);
    }
}
