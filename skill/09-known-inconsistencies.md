# 09 — Known Inconsistencies

> Don't copy these patterns. Follow the conventions in the other skill docs instead.

## Naming

- **`infra/intergration/`** — typo for `integration`. Don't rename (breaks imports). Use correct spelling for new packages.
- **`UseCaseConfig`** class vs **`Usecase`** interfaces — class uses CamelCase `UseCase`, interfaces use `Usecase`. Follow `Usecase` (lowercase 'c').

## Architecture Deviations

- **Jackson in core**: `ResourceCategoryModel`, `RoleModel` use `tools.jackson.databind.annotation.JsonSerialize`. For new models, put serialization annotations in API response DTOs.
- **Port in controller**: `AuthController` injects `NotificationService` port directly (for device token side-effects). Prefer encapsulating in use case.
- **Raw String return**: `InternalAuthController.getPublicKey()` returns `String` not `ResponseApi`. Always wrap in `ResponseApi`.
- **Missing validation**: `VerifyIdentityByUsernameRequest`, `SendOtpRequest`, `VerifyOtpRequest`, `FirstLoginResetPasswordRequest` lack `@NotBlank`. Always add validation on new DTOs.
- **Commented-out `@HasPermission`**: `AuthzController` endpoints have no permission checks. Don't copy this.

## Code

- **Custom Redis serialization**: `VerificationRequestStoreImpl` uses a fragile custom string format. Use JSON for new Redis-stored objects.
- **Password logged**: `AuthIdentityUsecaseImpl.createAuthIdentity()` logs the plaintext default password. Don't log secrets.
- **Inline dependency versions**: `commons-fileupload:1.6.0` and `commons-pool2:2.13.1` are hardcoded in `infra/build.gradle.kts`. Put new deps in `gradle/libs.versions.toml`.
- **Default secrets**: `INTERNAL_SECRET_KEY`, `REFRESH_TOKEN_SECRET_KEY`, `OTP_HMAC_SECRET` have insecure defaults in `application.yml`. Always use env vars in production.
