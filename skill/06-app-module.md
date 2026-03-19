# 06 — App Module

## Role

The **app** module is the **assembler**. It boots the application, wires use cases to their port implementations, holds all configuration, and manages database migrations.

**Package**: `com.intern.hub.auth.app`

## Rules

- **Only** module that has `bootJar` enabled
- **Only** place where use cases are registered as Spring beans
- `application.yml` is the single source of truth for all config
- Database migrations go in `db/changelog/` — **never** use `ddl-auto`

## Package Layout

```
app/
├── src/main/java/com/intern/hub/auth/app/
│   ├── AuthServiceApplication.java    # @SpringBootApplication entry point
│   ├── config/
│   │   └── UseCaseConfig.java         # Registers core use cases as @Bean
│   └── runner/
│       └── RedisCheck.java            # CommandLineRunner — pings Redis on startup
└── src/main/resources/
    ├── application.yml                # All configuration
    └── db/changelog/                  # Liquibase migrations (YAML)
        ├── db.changelog-master.yml
        ├── v1.0/
        └── v1.1/
```

## Dependencies

```kotlin
dependencies {
    implementation(project(":api"))
    implementation(project(":infra"))
    implementation(project(":core"))
    implementation(libs.bundles.custom.libraries)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.autoconfigure)
    implementation(libs.spring.boot.starter.liquibase)
    implementation(libs.spring.boot.starter.kafka)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
```

## Main Class Pattern

```java
@SpringBootApplication(
    exclude = {UserDetailsServiceAutoConfiguration.class},
    scanBasePackages = {
        "com.intern.hub.auth.app",
        "com.intern.hub.auth.api",
        "com.intern.hub.auth.infra",
        "com.intern.hub.auth.core",
    }
)
@EnableSecurity                     // From security-starter
@EnableGlobalExceptionHandler       // From common-library
@EnableJpaRepositories(basePackages = "com.intern.hub.auth.infra.persistence.repository.jpa")
@EntityScan(basePackages = "com.intern.hub.auth.infra.persistence.entity")
public class AuthServiceApplication { ... }
```

## How to Wire a Use Case

In `app/config/UseCaseConfig.java`:

```java
@Configuration
public class UseCaseConfig {

    @Bean
    public MyUsecase myUsecase(
            MyRepository myRepository,          // resolved from infra @Component
            SomeExternalService someService      // resolved from infra @Service
    ) {
        return new MyUsecaseImpl(myRepository, someService);
    }
}
```

Spring auto-resolves port parameters from infra beans. The app module passes them into core use case constructors.

**Whenever you add a new use case or add a new port dependency to an existing one, update this file.**

## How to Add a Liquibase Migration

1. Pick the version folder (create `vX.X/` if needed)
2. Create numbered YAML file: `00N-description.yml`
3. Include it in `db.changelog-vX.X.yml`
4. Include the version file in `db.changelog-master.yml`

```yaml
# db/changelog/vX.X/00N-create-my-table.yml
databaseChangeLog:
  - changeSet:
      id: 00N-create-my-table
      author: developer
      changes:
        - createTable:
            tableName: my_table
            columns:
              - column:
                  name: id
                  type: BIGINT
                  constraints:
                    primaryKey: true
                    nullable: false
              - column:
                  name: name
                  type: VARCHAR(255)
                  constraints:
                    nullable: false
              - column:
                  name: status
                  type: VARCHAR(50)
                  defaultValue: ACTIVE
                  constraints:
                    nullable: false
              - column:
                  name: version
                  type: INT
                  defaultValueNumeric: 0
                  constraints:
                    nullable: false
              - column: { name: created_at, type: BIGINT }
              - column: { name: updated_at, type: BIGINT }
              - column: { name: created_by, type: BIGINT }
              - column: { name: updated_by, type: BIGINT }
```

## How to Add Configuration

1. Add property in `application.yml` under a descriptive prefix
2. If core needs the value → define a config port in core, implement with `@ConfigurationProperties` in infra
3. If only infra needs it → use `@Value` or `@ConfigurationProperties` directly in infra

## BootJar Configuration

```kotlin
// app/build.gradle.kts
tasks.jar { enabled = false }
tasks.bootJar {
    enabled = true
    archiveFileName.set("auth-service.jar")
}
springBoot {
    mainClass.set("com.intern.hub.auth.app.AuthServiceApplication")
}
```

All other modules have `bootJar` disabled and plain `jar` enabled (set in root `build.gradle.kts`).

## Docker

- **Build**: `eclipse-temurin:25.0.2_10-jdk` → Gradle build → `jdeps` + `jlink` custom JRE
- **Runtime**: `gcr.io/distroless/base-debian12` with custom JRE, JVM: `-Xmx512m -Xms256m`
- **Docker Compose** (local dev): Redis + Kafka + Kafka UI. PostgreSQL NOT included — provide externally.
