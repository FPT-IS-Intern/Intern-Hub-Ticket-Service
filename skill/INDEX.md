# Intern Hub Auth Service — AI Agent Skill Index

> **Read this first.** This folder teaches you HOW to design and develop features in this codebase. For WHAT the code does, read the source.

## Architecture in One Sentence

```
Controller (api) → UseCase interface (core) → Port interface (core) → Implementation (infra)
```

Core = zero framework imports. Use cases = plain Java, wired as `@Bean` in `app/config/UseCaseConfig.java`.

---

## When You Need To…

| Task | Read |
|---|---|
| Understand the tech stack and dependencies | [00-overview.md](00-overview.md) |
| Understand module boundaries and rules | [01-structure.md](01-structure.md) |
| Use custom libraries (ResponseApi, exceptions, Snowflake, security) | [02-custom-library.md](02-custom-library.md) |
| Create a domain model, port, use case, enum, or exception code | [03-core-module.md](03-core-module.md) |
| Create a controller, request/response DTO | [04-api-module.md](04-api-module.md) |
| Implement a port (JPA, Redis, Kafka, Feign, config) | [05-infra-module.md](05-infra-module.md) |
| Wire a use case bean, add config, add a migration | [06-app-module.md](06-app-module.md) |
| Understand request/error/event flow patterns | [07-api-flows.md](07-api-flows.md) |
| Name a class, method, table, key, or topic | [08-naming-conventions.md](08-naming-conventions.md) |
| Avoid known pitfalls and inconsistencies | [09-known-inconsistencies.md](09-known-inconsistencies.md) |

---

## 10 Rules to Never Break

1. **Core module** → no `org.springframework.*`, `jakarta.persistence.*`, `org.apache.kafka.*`
2. **Use cases** → plain Java, registered as `@Bean` in `UseCaseConfig`
3. **All endpoints** → return `ResponseApi<T>`
4. **All entity IDs** → Snowflake (never auto-increment)
5. **Schema changes** → Liquibase YAML (never `ddl-auto`)
6. **DTOs** → Java records in `api` module only
7. **DI** → constructor only (`@RequiredArgsConstructor`), no `@Autowired`
8. **Jackson** → 3.x, import `tools.jackson.databind.*`
9. **Redis keys** → auto-prefixed with `auth:`, use logical keys in code
10. **Passwords** → salted as `userId + ":" + rawPassword` before hashing

---

## New Feature Checklist

When building a new feature, touch these files in order:

1. `core/domain/model/` — domain model + enum + exception code
2. `core/domain/port/` — port interfaces for external deps
3. `core/domain/usecase/` — use case interface + impl
4. `infra/persistence/entity/` — JPA entity
5. `infra/persistence/repository/jpa/` — JPA repository
6. `infra/mapper/` — MapStruct mapper
7. `infra/persistence/repository/impl/` or `infra/service/` — port implementations
8. `api/dto/request/` + `api/dto/response/` — DTOs
9. `api/controller/` — controller
10. `app/config/UseCaseConfig.java` — wire the use case bean
11. `app/src/main/resources/db/changelog/` — Liquibase migration
12. `app/src/main/resources/application.yml` — config / excluded paths
