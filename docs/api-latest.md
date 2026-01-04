API Overview (auth-service & event-service)
==========================================

Base URLs
---------
- Auth service: http://localhost:8081
- Event service: http://localhost:8080
- All requests use JSON. Authenticated calls add `Authorization: Bearer <token>`.

Auth Service (`/auth`)
----------------------
- POST `/auth/register`
  - Body: `{ "email": "...", "password": "...", "answer": "security answer" }`
  - 201: `{ "userId": "1", "message": "Registration successful", "token": "<jwt>" }`
  - 409 if email exists; 400 invalid payload.

- POST `/auth/login`
  - Body: `{ "email": "...", "password": "..." }`
  - 200: `{ "userId": "1", "message": "Login successful", "token": "<jwt>" }`
  - 401 invalid creds; 400 validation error.

- POST `/auth/reset-password`
  - Body: `{ "email": "...", "answer": "security answer", "newPassword": "..." }`
  - 200: `{ "message": "Password reset successful" }`
  - 400 invalid answer/user not found.

- GET `/auth/{id}`
  - 200: user object; 404 if not found.

- GET `/auth?email=...`
  - 200: user object; 404 if not found.

- Magic link/code (optional password-reset flow)
  - POST `/auth/send-magic-link` body `{ "email": "..." }` -> 200 message
  - POST `/auth/verify-magic-link` body `{ "email": "...", "verificationCode": "123456" }` -> 200 message / 401 invalid
  - POST `/auth/save-magic-link` body `{ "email": "...", "verificationCode": "123456" }` -> 200 message (updates password to code hash)

Event Service
-------------
- Auth: `Authorization: Bearer <token>` required for non-GET endpoints. GET /events can be anonymous (listing/read).

Events (`/events`)
- GET `/events`
  - Query filters optional: `category`, `date` (YYYY-MM-DD), `includeOutdated` (`yes`/`no`, default `no`).
  - 200: list of events.
- POST `/events`
  - Headers: Authorization required.
  - Body: `{ "eventName": "...", "eventLocation": { "latitude": 0, "longitude": 0 }, "eventDescription": "...", "eventDate": "YYYY-MM-DD", "eventTime": "HH:mm:ss", "eventCreatorId": 1 }`
  - 201: `{ "eventId": 1, "message": "Event created successfully" }`
- GET `/events/{id}`
  - 200 event; 404 not found.
- DELETE `/events/{eventId}`
  - Headers: Authorization required.
  - Body: `{ "userId": 1 }`
  - 200 success; 404 not found.
- PUT `/events/{eventId}`
  - Headers: Authorization required.
  - Body includes `eventCreatorId` and updated fields.
  - 200 updated; 403 if not creator; 404 not found; 400 bad format.

Comments (`/comments`)
- POST `/comments`
  - Body: `{ "content": "...", "postedBy": 1, "eventId": 1 }`
  - 201 created; 400 bad payload; 404 event not found.
- GET `/comments`
  - Query: `eventId`
  - 200 list; 404 no comments.
- DELETE `/comments/event/{eventId}`
  - 200 deleted; 404 none; 401 unauthorized (if enforced).
- PUT `/comments/{commentId}`
  - Headers: Authorization required.
  - Body: `{ "content": "...", "postedBy": 1 }`
  - 200 updated; 403 not owner; 404 not found.
- DELETE `/comments/{commentId}`
  - Headers: Authorization required.
  - Body: `{ "userId": 1 }`
  - 200 deleted; 403 not owner; 404 not found.

Validation (`/events/{eventId}`)
- POST `/confirm`
  - Body: `{ "userId": 1 }`
  - 201 created; 409 already validated.
- DELETE `/confirm`
  - Body: `{ "userId": 1 }`
  - 200 withdrawn; 404 not found.
- POST `/report-false`
  - Body: `{ "userId": 1 }`
  - 201 created; 409 already validated.
- DELETE `/report-false`
  - Body: `{ "userId": 1 }`
  - 200 withdrawn; 404 not found.
- GET `/validation-counts`
  - 200: `{ "eventId": 1, "confirmedCount": 10, "falseReportsCount": 5 }`
  - 404 event not found.
- GET `/validation-status/{userId}`
  - 200: `{ "validation-status": "confirmed|falseReport|notReport" }`
  - 404 event/user not found.

Auth/Token notes
----------------
- Tokens are JWT signed with HS256; shared secret `app.jwt.secret` must match across services.
- Include `Authorization: Bearer <token>` for protected endpoints (all POST/PUT/DELETE and most GET except event listing).
