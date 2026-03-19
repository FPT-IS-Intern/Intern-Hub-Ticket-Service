# 01 — Project Structure

## Module Dependency Graph

```
app ──→ api ──→ core
app ──→ infra ──→ core
app ──→ core

core ──→ (nothing)
```

- `core` **never** depends on any other module
- `api` and `infra` depend **only** on `core` — never on each other
- `app` depends on **all** modules — it's the assembler

## Module Roles

| Module | Role | Contains |
|---|---|---|
| `core` | Domain logic | Models, ports (interfaces), use cases. Zero framework imports. |
| `api` | Driving adapter | Controllers, DTOs, input validation. Calls use case interfaces. |
| `infra` | Driven adapter | JPA entities, Redis/Kafka/Feign impls, mappers, config. Implements port interfaces. |
| `app` | Assembler | Main class, `UseCaseConfig` (wires beans), `application.yml`, Liquibase. |

## MUST

- Keep `core` free from `org.springframework.*`, `jakarta.persistence.*`, `org.apache.kafka.*`
- Define business logic in `core/domain/usecase/impl/`
- Define external deps as port interfaces in `core/domain/port/`, implement in `infra/`
- Register use cases as `@Bean` in `app/config/UseCaseConfig.java`
- Use version catalog (`gradle/libs.versions.toml`) for all dependency versions
- Return `ResponseApi<T>` from all REST endpoints
- Use Snowflake IDs for all entity primary keys
- Use Liquibase YAML for all schema changes
- Use `record` types for DTOs and immutable domain models
- Use `@RequiredArgsConstructor` for dependency injection
- Map JPA Entity ↔ Domain Model at the infra boundary (via MapStruct)
- Map Request DTO → use case input and use case output → Response DTO at the API boundary
- Place `@Transactional` in infra or API layer — never in core
- Place format validation (`@NotBlank`, `@Valid`) in API DTOs. Business validation in core.
- Publish Kafka events only through core ports with infra implementations

## MUST NOT

- Import Spring/JPA/Kafka/Redis in `core`
- Let JPA entities leak into core
- Put business logic in controllers
- Annotate core use cases with `@Service` / `@Component`
- Use `@Autowired` field injection
- Return JPA entities from REST endpoints
- Call repositories directly from controllers
- Use `ddl-auto: update` or `create`
- Put `@ConfigurationProperties` in core — use config port interfaces instead
