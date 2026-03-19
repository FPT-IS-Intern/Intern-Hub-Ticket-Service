# 03 — Core Module

## Role

The **core** module contains **business logic, domain models, and port interfaces**. It is completely **framework-agnostic**.

**Package**: `com.intern.hub.auth.core.domain`

## Rules

- **ZERO** Spring / JPA / Kafka / Redis imports
- Allowed dependencies: Java stdlib, Lombok, MapStruct, custom common-library (for exceptions, `RandomGenerator`, `Snowflake`)
- Use cases are **plain Java classes** — never annotated `@Service` / `@Component`
- All external operations are accessed through **port interfaces** defined here

## Package Layout

```
core/domain/
├── model/             # Domain models (POJOs, records)
│   ├── command/       # Command objects for write operations
│   ├── enums/         # Domain enums
│   └── exception/     # ExceptionCode string constants
├── port/              # Port interfaces (driven side)
└── usecase/
    ├── *.java         # Use case interfaces
    └── impl/          # Use case implementations
```

## How to Create a Domain Model

**Mutable model** (has lifecycle, gets updated):

```java
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder                              // SuperBuilder because it extends AuditModel
@FieldDefaults(level = PRIVATE)
public class MyModel extends AuditModel {
    Long id;
    String name;
    @Setter(AccessLevel.NONE) Integer version;  // read-only for optimistic locking
    @Builder.Default MyStatus status = MyStatus.ACTIVE;
}
```

**Immutable model** (value object, query result):

```java
public record MyResult(Long id, String data) {}
```

### Key conventions
- Mutable models extend `AuditModel` → fields: `createdAt`, `updatedAt`, `createdBy`, `updatedBy` (all `Long`)
- Use `@SuperBuilder` when extending `AuditModel`, `@Builder` otherwise
- `version` field → `@Setter(AccessLevel.NONE)` (managed by JPA, read-only in domain)
- Enum fields → `@Builder.Default` with a default value
- `Map<String, Object>` for JSONB-backed fields (e.g. credential storage)

### Known inconsistency
`ResourceCategoryModel` and `RoleModel` use `tools.jackson.databind.annotation.JsonSerialize` — a framework leak. For new models, handle serialization in API response DTOs instead.

## How to Create a Port Interface

Ports define WHAT the core needs from the outside. Infra implements HOW.

**Repository port** (data access):
```java
public interface MyRepository {
    MyModel findById(Long id);
    MyModel save(MyModel model);
    List<MyModel> findByStatus(MyStatus status);
}
```

**Service port** (external operation):
```java
public interface MyExternalService {
    void doSomething(Long entityId, String data);
    String fetchSomething(Long id);
}
```

**Config port** (externalized configuration):
```java
public interface MyFeatureConfig {
    int getMaxRetries();
    long getTimeoutSeconds();
}
```

**Operations port** (pure utility):
```java
public interface MyOperations {
    String generate(int length);
    boolean verify(String input, String stored);
}
```

### Naming conventions
| Type | Pattern | Example |
|---|---|---|
| Data access | `{Domain}Repository` | `AuthIdentityRepository` |
| External call | `{Domain}Service` | `AccessTokenService`, `NotificationService` |
| Configuration | `{Domain}Config` or `{Domain}Policy` | `OtpConfig`, `AuthenticationPolicy` |
| Utility | `{Domain}Operations` | `OtpOperations` |
| Storage | `{Domain}Store` | `OtpStore`, `VerificationRequestStore` |

## How to Create a Use Case

### 1. Define interface

```java
package com.intern.hub.auth.core.domain.usecase;

public interface MyUsecase {
    MyResult doSomething(String input);
    void performAction(Long id, String data);
}
```

**Naming**: `{Domain}Usecase` (lowercase 'c' — project convention).

### 2. Implement

```java
package com.intern.hub.auth.core.domain.usecase.impl;

@RequiredArgsConstructor
public class MyUsecaseImpl implements MyUsecase {

    // Inject ONLY port interfaces — never concrete classes
    private final MyRepository myRepository;
    private final SomeExternalService someService;

    @Override
    public MyResult doSomething(String input) {
        // Business logic here
        // Throw domain exceptions from common-library:
        //   throw new NotFoundException(ExceptionCode.MY_CODE, "message");
        //   throw new BadRequestException(ExceptionCode.MY_CODE);
    }
}
```

### 3. Register in app module

In `app/config/UseCaseConfig.java`:
```java
@Bean
public MyUsecase myUsecase(MyRepository myRepository, SomeExternalService someService) {
    return new MyUsecaseImpl(myRepository, someService);
}
```

### Key conventions
- **No Spring annotations** on use case classes
- Constructor injection via `@RequiredArgsConstructor`
- Throw exceptions from common-library: `BadRequestException`, `NotFoundException`, `UnauthorizeException`, `ForbiddenException`, `TooManyRequestException`, `ConflictDataException`
- Use `ExceptionCode` constants as the first argument
- **No `@Transactional`** in core — that belongs in infra or api layer
- Input validation (format checks) → API DTOs. Business validation → use cases.

## How to Add an ExceptionCode

In `core/domain/model/exception/ExceptionCode.java`:

```java
public static final String MY_NEW_ERROR = "auth.exception.my_new_error";
```

Pattern: `auth.exception.{snake_case_description}`

## How to Add an Enum

In `core/domain/model/enums/`:

```java
public enum MyStatus {    // name pattern: {Domain}Status or {Domain}Type
    ACTIVE,
    INACTIVE
}
```

## Password Salting Convention

All passwords are salted before hashing: `userId + ":" + rawPassword`. This is done at the use case / credential service boundary, not inside the hasher.
