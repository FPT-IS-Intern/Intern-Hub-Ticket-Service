# 08 — Naming Conventions

> When in doubt, follow these rules rather than copy existing code — the project has some inconsistencies.

## Classes

| What | Pattern | Example |
|---|---|---|
| Controller | `{Domain}Controller` | `AuthController`, `AuthzController` |
| Internal controller | `Internal{Domain}Controller` | `InternalAuthController` |
| Request DTO | `{Action}{Domain}Request` | `UserpassLoginRequest`, `CreateUserPassIdentityRequest` |
| Response DTO | `{Domain}Response` | `LoginResponse`, `SendOtpResponse` |
| Use case interface | `{Domain}Usecase` | `AuthenticationUsecase` (lowercase 'c') |
| Use case impl | `{Domain}UsecaseImpl` | `AuthenticationUsecaseImpl` |
| Port: repository | `{Domain}Repository` | `AuthIdentityRepository` |
| Port: service | `{Domain}Service` | `AccessTokenService` |
| Port: config | `{Domain}Config` or `{Domain}Policy` | `OtpConfig`, `AuthenticationPolicy` |
| Port: operations | `{Domain}Operations` | `OtpOperations` |
| Port: store | `{Domain}Store` | `OtpStore` |
| Port impl: repo | `{Port}Impl` | `AuthIdentityRepositoryImpl` |
| Port impl: service | `{Port}Impl` | `AccessTokenServiceImpl` |
| Port impl: config | `{Domain}Properties` or `{Domain}Provider` | `OtpProperties`, `AuthenticationPolicyProvider` |
| JPA entity | `{TableName}` (PascalCase) | `AuthIdentity`, `RefreshToken` |
| JPA repository | `{Entity}JpaRepository` | `AuthIdentityJpaRepository` |
| MapStruct mapper | `{Domain}Mapper` | `AuthIdentityMapper` |
| Mutable domain model | `{Domain}Model` | `AuthIdentityModel`, `UserModel` |
| Immutable domain model | Record, no suffix | `AuthModel`, `TokenClaims`, `PermissionModel` |
| Command | `{Action}Command` | `CreateIdentityCommand` |
| Enum | `{Domain}Status` or `{Domain}Type` | `AuthIdentityStatus`, `AuthProviderType` |
| Configuration | `{Domain}Configuration` | `RedisConfiguration`, `KafkaProducerConfiguration` |
| Properties | `{Domain}Properties` | `AccessTokenProperties`, `OtpProperties` |
| Utility | `{Domain}Helper` | `TokenHelper`, `OtpHelper` |
| Exception code | `{DOMAIN}_{DESCRIPTION}` (constant) | `WRONG_CREDENTIALS`, `OTP_MAX_ATTEMPTS` |

## Methods

| Context | Pattern | Examples |
|---|---|---|
| Controller action | Verb | `login()`, `logout()`, `refreshToken()` |
| Controller read | `get{Thing}` | `getPublicKey()`, `getResourceCategories()` |
| Controller write | `create/lock/unlock{Thing}` | `createUserpassIdentity()` |
| Use case action | Business verb | `authenticate()`, `resetPassword()` |
| Repository query | `findBy*()` | `findByProviderAndIdentifier()` |
| Repository write | `save()` | |
| Token/ID creation | `generate*()` | `generateAccessToken()`, `generateOtp()` |
| Invalidation | `revoke*()` | `revokeAccessToken()`, `revokeRefreshToken()` |
| Validation | `verify*()` | `verifyCredential()`, `verifyOtp()` |
| Hashing | `hash()` / `verify()` | `hash()`, `verify()` |

## Database

- Tables: `snake_case` — `auth_identity`, `refresh_token`, `user_role`
- Columns: `snake_case` — `user_id`, `expired_at`, `sys_status`
- Enums: `VARCHAR` with `@Enumerated(EnumType.STRING)` — stored as uppercase

## Redis Keys

- Pattern: `{category}:{identifier}` (auto-prefixed with `auth:`)
- Examples: `blacklist:token:{jti}`, `login-fail:{username}`, `request:{requestId}:otp`

## Kafka Topics

- Pattern: `{service}.{entity}.{event-type}`
- Examples: `notification.event.request`, `auth.device-token.event`

## Config Properties

- YAML: `kebab-case` — `auth.access-token.expiration-seconds`
- Java: `camelCase` — `expirationSeconds`
- Env vars: `SCREAMING_SNAKE_CASE` — `ACCESS_TOKEN_PRIVATE_KEY`

## Gradle

- Module names: lowercase — `api`, `core`, `infra`, `app`
- Version catalog: `kebab-case` — `spring-boot-starter-web`
- Bundles: `kebab-case` — `spring-boot-all`, `custom-libraries`
