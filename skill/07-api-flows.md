# 07 — Request Flow Architecture

## How a Request Flows Through the System

Every API call follows the same pattern:

```
HTTP Request
  → Controller (api)          validates input, extracts headers
    → UseCase (core)          orchestrates business logic
      → Port calls (core)     abstract external operations
        → Impl (infra)        JPA / Redis / Kafka / Feign
    ← Domain result
  ← ResponseApi<T>
```

## Typical Public Endpoint Flow

```
Client
  │  Body: JSON, Headers: X-Device-ID, etc.
  ▼
Controller (@RestController)
  │  @RequestBody @Valid → DTO validation
  │  Extract headers via @RequestHeader
  ▼
UseCase.method(mapped args)
  │  Business rules, orchestration
  │  Throws domain exceptions on failure
  │  Calls port interfaces:
  │    ├── Repository port  → infra → JPA → PostgreSQL
  │    ├── Service port     → infra → Redis / Kafka / Feign
  │    └── Config port      → infra → @ConfigurationProperties
  ▼
Controller maps result → Response DTO
  ▼
ResponseApi.ok(responseDTO) or ResponseApi.noContent()
```

## Endpoint Security Model

Three levels of access control:

### Public endpoints
- Path listed in `security.excluded-paths` in `application.yml`
- No authentication, no secret — fully open
- Example: `/auth/login`, `/auth/health`

### Authenticated endpoints (`@Authenticated`)
- Requires a valid JWT (validated via gateway headers: `X-JTI`, `X-User-Id`)
- Does **not** check any specific permission — just proves the caller is logged in
- Example: `POST /auth/logout`

### Permission-protected endpoints (`@HasPermission`)
- Requires valid JWT **and** a specific RBAC permission
- Example: `@HasPermission(action = "read", resource = "permissions")`

### Internal endpoints — two mechanisms

**1. Auto-secured by path prefix** (`security.internal-path-prefix`):
- Any path starting with `/auth/internal/**` is automatically secured by the security-starter filter
- The filter validates the `X-Internal-Secret` header — no annotation needed
- No JWT required — callers are other microservices, not users

**2. `@Internal` annotation** (for internal endpoints outside the prefix):
- Use `@Internal` on a controller method when the path does NOT start with `internal-path-prefix` but still needs `X-Internal-Secret` validation
- This is for internal endpoints that live under a different path structure

## Error Flow

```
UseCase throws domain exception
  → @EnableGlobalExceptionHandler (common-library) catches it
    → Maps to HTTP status:
        BadRequestException     → 400
        UnauthorizeException    → 401
        ForbiddenException      → 403
        NotFoundException       → 404
        ConflictDataException   → 409
        TooManyRequestException → 429
        InternalErrorException  → 500
  → Returns ResponseApi with error code + message
```

## Kafka Event Flow (Outgoing)

```
UseCase calls NotificationService.sendOtp(email, otp)
  → NotificationServiceImpl builds message record
    → KafkaTemplate.send(topic, payload)
      → Kafka broker (async, fire-and-forget)
```

Notification event structure:
```json
{
  "eventId": "<snowflake>",
  "payload": { ... },
  "channels": ["EMAIL"],
  "sourceService": "auth-service",
  "recipients": { "EMAIL": "user@example.com" },
  "metadata": { "locale": "vi" },
  "notificationType": "<template-type>"
}
```

## Inter-Service HTTP Flow (Outgoing)

```
UseCase calls UserClientService.getUserInfo(userId)
  → UserClientServiceImpl delegates to UserFeignClient
    → Feign sends GET /hrm/internal/users/{id}
      (X-Internal-Secret header auto-added by FeignConfiguration)
    → ResponseApi<UserClientModel> response
  → UserMapper maps Feign model → Domain model
  → Returns to UseCase
```

## Flow Pattern: Multi-Step Stateful Process

Some features (like password reset) span multiple HTTP calls with shared state:

```
Step 1: POST /verify    → creates session in Redis (UUID requestId) → returns requestId
Step 2: POST /send-otp  → loads session → generates OTP → stores hash in Redis → sends via Kafka
Step 3: POST /verify-otp → loads session → verifies OTP hash → marks session verified
Step 4: POST /reset     → loads session → checks verified → performs action → deletes session
```

State is stored in Redis with TTL. Each step loads the session by `requestId`, validates preconditions, and updates the session.
