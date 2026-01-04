Login service extraction plan
============================

Scope and existing auth pieces
------------------------------
- Controllers: `src/main/java/com/steve/controller/UserController.java` (`/auth/register`, `/auth/login`, `/auth/reset-password`, `/auth/{id}`, `/auth?email=`) and `src/main/java/com/steve/controller/MagicLinkController.java` (`/auth/send-magic-link`, `/auth/verify-magic-link`, `/auth/save-magic-link`).
- Services: `UserService` (register/login/reset-password/user lookup) and `MagicLinkService` (email verification code, password reset via code).
- Mappers: `UserMapper`, `SecurityAnswerMapper`, `VerificationTokenMapper`.
- Entities/DTOs: `User`, `VerificationToken`, login/registration/reset DTOs under `src/main/java/com/steve/dto/`.
- Config: DB + mail in `src/main/resources/application.yml`.

What to move into the new login service
---------------------------------------
- REST layer: `UserController`, `MagicLinkController`.
- Service layer: `UserService`, `MagicLinkService`.
- Persistence: `UserMapper`, `SecurityAnswerMapper`, `VerificationTokenMapper` and associated tables (`User`, `SecurityAnswer`, `VerificationToken`).
- DTOs/entities used only by auth flows (`LoginRequest/Response`, `UserRegistrationRequest`, `RegistrationResponse`, `PasswordResetRequest`, `ErrorResponse`, `VerificationToken`, `User`).
- Email delivery settings and `app.magic-link.base-url` config.
- Remove direct user-table access from other services; they should consume the login service HTTP APIs instead.

Target API contract (JWT-based)
-------------------------------
Base path: `/auth`. All responses JSON.

- `POST /auth/register`
  - Request: `{ "email": "user@example.com", "password": "string", "answer": "security answer" }`
  - Response 201: `{ "userId": "123", "message": "Registration successful" }`
  - Errors: `409` (email exists), `400` (bad payload).

- `POST /auth/login`
  - Request: `{ "email": "user@example.com", "password": "string" }`
  - Response 200: `{ "userId": "123", "accessToken": "<jwt>", "expiresIn": 3600 }`
  - Errors: `401` invalid credentials, `400` validation error.

- `POST /auth/refresh` (new)
  - Request: `{ "refreshToken": "<jwt>" }`
  - Response 200: `{ "accessToken": "<jwt>", "expiresIn": 3600 }`
  - Errors: `401` invalid/expired token.

- `POST /auth/reset-password`
  - Request: `{ "email": "user@example.com", "answer": "security answer", "newPassword": "string" }`
  - Response 200: `{ "message": "Password reset successful" }`
  - Errors: `400` invalid answer/user not found.

- Magic link / code flow (optional to keep)
  - `POST /auth/send-magic-link` body: `{ "email": "user@example.com" }` -> 200 `{ "message": "Verification code sent" }`
  - `POST /auth/verify-magic-link` body: `{ "email": "user@example.com", "verificationCode": "123456" }` -> 200 `{ "message": "Code verified" }`
  - `POST /auth/save-magic-link` body: `{ "email": "user@example.com", "verificationCode": "123456" }` -> 200 `{ "message": "Password updated" }`

JWT strategy
------------
- Signer: HMAC-SHA256 with secret env `JWT_SECRET`.
- Claims: `sub` (userId), `email`, `iat`, `exp` (e.g., 15 min for access token), `jti`.
- Refresh token: longer TTL (e.g., 7–30 days); store `jti` server-side to allow revocation.
- Header: `Authorization: Bearer <accessToken>` for protected endpoints in other services.
- Clock skew: allow small leeway (e.g., 60s) when validating.

Data ownership
--------------
- Login service owns: `User`, `SecurityAnswer`, `VerificationToken`, password hashing (BCrypt), email sender config.
- Other services: never touch user tables directly; rely on `/auth` APIs and validate JWTs with shared `JWT_SECRET` (or via JWKS if later).

Migration steps (short-term)
----------------------------
1) Create a new Maven module/repo `login-service` with Spring Boot starter web + security + mail + mybatis; copy over the auth controllers/services/mappers/entities/DTOs listed above.
2) Add JWT issuing/validation (controller/service) and refresh-token persistence (simple DB table or in-memory cache initially).
3) Expose `/auth/login` to return JWTs; adjust `LoginResponse` to include `accessToken`, `expiresIn`, optional `refreshToken`.
4) Add a token verification endpoint for other services (optional if they validate locally with shared secret).
5) In the remaining services, replace direct `UserMapper` usage with HTTP calls to the login service; introduce a common auth filter to enforce `Authorization` header.
6) Provide `.env`/application properties for `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`, mail settings.
7) Build a docker-compose for local dev with login-service + app-service + DB + mailcatcher (for local email).

Notes for implementation
------------------------
- Hash all passwords with BCrypt (already used).
- Clean up `MagicLinkService.convertStringToMap` by switching to proper JSON request binding.
- Replace plaintext password reset via code-with-password with a token-based reset flow for security.
