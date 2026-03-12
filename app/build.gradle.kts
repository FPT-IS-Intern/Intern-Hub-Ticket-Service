dependencies {
    // Project dependencies
    implementation(project(":api"))
    implementation(project(":infra"))
    implementation(project(":core"))

    // Custom Libraries - common across all modules
    implementation(libs.bundles.custom.libraries)

    // Spring Boot dependencies
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.autoconfigure)
    // Liquibase
    implementation(libs.spring.boot.starter.liquibase)
    // Kafka
    implementation(libs.spring.boot.starter.kafka)
    // Feign
    implementation(libs.spring.boot.starter.feign)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
    archiveFileName.set("ticket-service.jar")
}

springBoot {
    mainClass.set("com.intern.hub.ticket.app.TicketServiceApplication")
}

