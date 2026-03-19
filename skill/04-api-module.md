# 04 — API Module

## Role

The **api** module is the **driving adapter** — REST controllers, request/response DTOs, input validation. It calls use case interfaces and maps results to responses.

**Package**: `com.intern.hub.auth.api`

## Rules

- Controllers contain **no business logic** — only: validate input → call use case → map response
- All endpoints return `ResponseApi<T>` from common-library
- DTOs are **Java records** and live only in this module — they never enter core
- Use case beans are NOT registered here — that's the app module's job
- Jackson is **3.x** → import from `tools.jackson.databind.*` (not `com.fasterxml.jackson.*`)

## Package Layout

```
api/
├── controller/        # REST controllers
├── dto/
│   ├── request/       # Request DTOs (records + validation)
│   └── response/      # Response DTOs (records)
└── config/            # OpenAPI configuration
```

## Dependencies

```kotlin
dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.custom.libraries)
    implementation(libs.bundles.spring.boot.all)  // web + validation
    implementation(libs.openapi.doc)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
```

## How to Create a Controller

```java
@RestController
@RequestMapping("/auth/my-feature")
@RequiredArgsConstructor
public class MyFeatureController {

    private final MyUsecase myUsecase;

    // Public endpoint
    @PostMapping
    public ResponseApi<MyResponse> create(@RequestBody @Valid MyRequest request) {
        var result = myUsecase.doSomething(request.name());
        return ResponseApi.ok(new MyResponse(result.id(), result.name()));
    }

    // Authenticated endpoint
    @GetMapping("/{id}")
    @Authenticated
    public ResponseApi<MyResponse> getById(@PathVariable Long id) {
        var result = myUsecase.findById(id);
        return ResponseApi.ok(new MyResponse(result.id(), result.name()));
    }

    // Permission-protected endpoint
    @DeleteMapping("/{id}")
    @HasPermission(action = "delete", resource = "my-feature")
    public ResponseApi<?> delete(@PathVariable Long id) {
        myUsecase.delete(id);
        return ResponseApi.noContent();
    }
}
```

### Security annotations
| Annotation | Usage |
|---|---|
| _(none)_ | Public endpoint (must be in `security.excluded-paths`) |
| `@Authenticated` | Requires valid JWT — proves caller is logged in, no specific permission needed |
| `@HasPermission(action, resource)` | Requires valid JWT **and** a specific RBAC permission |
| `@Internal` | Requires `X-Internal-Secret` header — use for internal endpoints whose path does NOT start with `security.internal-path-prefix` |

> Paths under `security.internal-path-prefix` (`/auth/internal/**`) are auto-secured by the security-starter filter — no `@Internal` annotation needed.

### Custom headers used by this project
| Header | Where | Required | Purpose |
|---|---|---|---|
| `X-Device-ID` | Login, refresh, logout | Yes | Unique device identifier |
| `X-Device-Type` | Login, logout | No | Device type (mobile, web) |
| `X-Device-Token` | Login, logout | No | Push notification token |
| `X-JTI` | Logout | Yes | JWT ID from access token |

## How to Create a Request DTO

```java
public record MyRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotNull(message = "ID is required")
    Long someId
) {}
```

- Use `jakarta.validation.constraints.*` for format validation
- Use `@Valid` on the controller parameter to trigger validation
- Business validation belongs in the use case, not here

## How to Create a Response DTO

```java
public record MyResponse(
    @JsonSerialize(using = ToStringSerializer.class)   // prevents JS precision loss
    Long id,

    String name
) {}
```

- **Always** use `@JsonSerialize(using = ToStringSerializer.class)` on `Long` fields
- Import from `tools.jackson.databind.annotation.JsonSerialize` and `tools.jackson.databind.ser.std.ToStringSerializer`

## Response Patterns

```java
// 200 with data
return ResponseApi.ok(myData);

// 204 no content
return ResponseApi.noContent();

// Errors: throw domain exceptions — the global handler maps them automatically
throw new NotFoundException(ExceptionCode.MY_CODE, "Not found");
```

## If a New Public Endpoint Is Created

Add its path to `security.excluded-paths` in `app/src/main/resources/application.yml`:

```yaml
security:
  excluded-paths:
    - /auth/my-new-public-endpoint
```

## Known Inconsistencies

- `InternalAuthController.getPublicKey()` returns raw `String`, not `ResponseApi` — don't copy this
- `AuthController` injects `NotificationService` port directly for device token side-effects — prefer encapsulating in use case
- Some request DTOs lack validation annotations — always add them for new DTOs
- `AuthzController` has `@HasPermission` commented out — endpoints are unprotected
