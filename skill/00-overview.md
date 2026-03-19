# 00 — Project Overview

## Purpose

**Intern Hub Auth Service** — a microservice for **authentication and authorization** in the Intern Hub platform. Manages login, JWT issuance (ED25519), RBAC, identity lifecycle, password reset (OTP), and exposes internal APIs for other services.

Does **not** manage user profiles — delegates to HRM Service via Feign.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | **Java 25** (`--parameters`, virtual threads) |
| Framework | **Spring Boot 4.0.3** |
| Build | **Gradle 9.2.1** (Kotlin DSL) + version catalog (`libs.versions.toml`) |
| Architecture | **Hexagonal** — 4 modules: `api`, `core`, `infra`, `app` |
| Database | **PostgreSQL** (Spring Data JPA + Hibernate) |
| Migrations | **Liquibase** (YAML) |
| Cache | **Redis** (Lettuce + connection pool) |
| Messaging | **Apache Kafka** (producer only, no consumers currently) |
| Inter-service | **Spring Cloud OpenFeign 5.0.1** |
| Security | Custom JWT (ED25519, no Spring Security filters) — uses `Intern-Hub-Security-Starter` |
| Mapping | **MapStruct 1.7.0.Beta1** |
| Boilerplate | **Lombok 1.18.42** |
| API Docs | **SpringDoc OpenAPI 3.0.1** |
| JSON | **Jackson 3.x** (`tools.jackson.databind.*`, NOT `com.fasterxml.jackson.*`) |
| IDs | **Snowflake** (from common library) |
| Docker | Eclipse Temurin JDK 25, custom JRE via `jlink`, distroless runtime |

## Key Design Decisions

1. **Core module has zero framework imports** — business logic is framework-agnostic
2. **Use cases are plain Java classes** — instantiated manually via `@Bean` factory methods in `UseCaseConfig`
3. **JWT is self-built** — no JWT library, ED25519 signing via `java.security.Signature`
4. **Redis keys auto-prefixed** with `${spring.application.name}:` (= `auth:`)
5. **Virtual threads** enabled for all request handling
6. **Two custom JitPack libraries**: `Intern-Hub-Common-Library` (exceptions, ResponseApi, Snowflake) and `Intern-Hub-Security-Starter` (@EnableSecurity, @Authenticated, AuditEntity)
7. **No tests** currently exist

## Dependencies

### Plugins
| Plugin | Version |
|---|---|
| `org.springframework.boot` | 4.0.3 |
| `io.spring.dependency-management` | 1.1.7 |

### Libraries
| Library | Version |
|---|---|
| `spring-cloud-starter-openfeign` | 5.0.1 |
| `springdoc-openapi-starter-webmvc-ui` | 3.0.1 |
| `postgresql` | 42.7.9 |
| `lombok` | 1.18.42 |
| `mapstruct` | 1.7.0.Beta1 |
| `Intern-Hub-Common-Library` | 2.1.1 |
| `Intern-Hub-Security-Starter` | 1.0.9 |
| `commons-pool2` | 2.13.1 |

Spring Boot starters (web, data-jpa, validation, security, kafka, data-redis, liquibase) are managed by Spring Boot 4.0.3.
