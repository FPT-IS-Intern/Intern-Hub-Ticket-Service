package com.intern.hub.ticket.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "SERVER_PORT=8080",
        "DB_HOST=localhost",
        "DB_PORT=5432",
        "DB_NAME=testdb",
        "DB_USERNAME=postgres",
        "DB_PASSWORD=123456",
        "SCHEMA_NAME=ih_ticket",
        "DEPLOY_ENV=dev",
        "INTERNAL_SECRET_KEY=intern_hub_ticket_secret_2026",
        "GATEWAY_URL=http://localhost:8765"
})
class TicketServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
