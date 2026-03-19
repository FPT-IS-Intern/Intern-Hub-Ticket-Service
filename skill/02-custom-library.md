# 02 — Custom Libraries

Two JitPack libraries from the `FPT-IS-Intern` GitHub organization. All 4 modules depend on both via the `custom-libraries` bundle.

## 1. Intern-Hub-Common-Library (v2.1.1)

`com.github.FPT-IS-Intern:Intern-Hub-Common-Library:2.1.1`

### What to use and how

**ResponseApi** — wrap all REST responses:
```java
return ResponseApi.ok(data);       // 200 with data
return ResponseApi.noContent();    // 204
```

**Exceptions** — throw from use cases/services, auto-mapped to HTTP status:
```java
throw new BadRequestException(ExceptionCode.MY_CODE, "message");     // 400
throw new UnauthorizeException(ExceptionCode.MY_CODE, "message");    // 401
throw new ForbiddenException(ExceptionCode.MY_CODE, "message");      // 403
throw new NotFoundException("Not found");                             // 404
throw new ConflictDataException(ExceptionCode.MY_CODE, "message");   // 409
throw new TooManyRequestException(ExceptionCode.MY_CODE, "message"); // 429
throw new InternalErrorException("Something broke");                  // 500
```

First arg (optional) is an error `code` string. Second is the `message`.

**Snowflake** — generate IDs:
```java
private final Snowflake snowflake;
Long newId = snowflake.next();
```

**RandomGenerator**:
```java
RandomGenerator.randomAlphaNumericString(12);        // random string
RandomGenerator.randomNumberString(6, false);        // random digits
RandomGenerator.generateSecretKey();                  // random secret
```

**`@EnableGlobalExceptionHandler`** — on main class, auto-catches domain exceptions.

## 2. Intern-Hub-Security-Starter (v1.0.9)

`com.github.FPT-IS-Intern:Intern-Hub-Security-Starter:1.0.9`

**`@EnableSecurity`** — on main class. Sets up:
- Auto-secures all paths under `security.internal-path-prefix` (e.g. `/auth/internal/**`) — validates `X-Internal-Secret` header automatically, no annotation needed
- Excluded paths (`security.excluded-paths`) bypass all authentication

**`@Authenticated`** — on controller methods. Requires a valid JWT (proves caller is logged in). Does **not** check any specific permission.

**`@HasPermission(action, resource)`** — on controller methods. Requires valid JWT **and** a specific RBAC permission.

**`@Internal`** — on controller methods whose path does NOT start with `security.internal-path-prefix`. Validates `X-Internal-Secret` header. Use this when an internal endpoint lives outside the auto-secured prefix path.

**`AuditEntity`** — base class for JPA entities. Provides `createdAt`, `updatedAt`, `createdBy`, `updatedBy` (all `Long`).

### Configuration

```yaml
security:
  internal-secret: ${INTERNAL_SECRET_KEY:defaultSecretValue}
  internal-path-prefix: /auth/internal
  excluded-paths:
    - /auth/health
    - /auth/login
    - /v3/api-docs/**
    - /swagger-ui/**
```

## Adding to a module

```kotlin
dependencies {
    implementation(libs.bundles.custom.libraries)
}
```

JitPack repo is configured in root `build.gradle.kts`.
