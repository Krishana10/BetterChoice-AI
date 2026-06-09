# BetterChoice AI — Authentication Flow

## Overview

BetterChoice AI uses **stateless JWT authentication** with short-lived access tokens and long-lived refresh tokens. Passwords are hashed with BCrypt. Spring Security filter chain validates every protected request.

## Token Strategy

| Token | Lifetime | Storage (Frontend) | Purpose |
|-------|----------|-------------------|---------|
| Access Token | 15 minutes | Memory / sessionStorage | API authorization |
| Refresh Token | 7 days | HttpOnly cookie (preferred) or secure storage | Obtain new access token |

## Registration Flow

```mermaid
sequenceDiagram
    participant U as User (Browser)
    participant F as React App
    participant A as AuthController
    participant S as AuthService
    participant DB as PostgreSQL

    U->>F: Submit register form
    F->>A: POST /api/v1/auth/register
    A->>S: register(RegisterRequest)
    S->>S: Validate email uniqueness
    S->>S: BCrypt hash password
    S->>DB: Save User (ROLE_USER)
    S->>S: Generate access + refresh JWT
    S->>DB: Save RefreshToken (hashed)
    S-->>A: AuthResponse
    A-->>F: 201 + tokens + user
    F->>F: Store tokens, set AuthContext
    F-->>U: Redirect to dashboard
```

## Login Flow

```mermaid
sequenceDiagram
    participant F as React App
    participant A as AuthController
    participant S as AuthService
    participant J as JwtProvider
    participant DB as PostgreSQL

    F->>A: POST /api/v1/auth/login {email, password}
    A->>S: login(LoginRequest)
    S->>DB: findByEmail
    S->>S: BCrypt matches?
    alt Invalid credentials
        S-->>A: InvalidCredentialsException (401)
    end
    S->>J: generateAccessToken(user)
    S->>J: generateRefreshToken(user)
    S->>DB: persist RefreshToken
    S-->>A: AuthResponse
    A-->>F: 200 + tokens
```

## Protected Request Flow

```mermaid
sequenceDiagram
    participant F as React App
    participant AF as JwtAuthFilter
    participant SC as SecurityContext
    participant C as Controller

    F->>AF: GET /api/v1/users/me<br/>Authorization: Bearer {access}
    AF->>AF: Parse & validate JWT signature
    AF->>AF: Check expiration
    AF->>AF: Extract userId, roles
    AF->>SC: Set Authentication
    AF->>C: Forward request
    C->>C: @PreAuthorize checks role
    C-->>F: 200 UserResponse
```

## Token Refresh Flow

```mermaid
sequenceDiagram
    participant F as React App
    participant I as Axios Interceptor
    participant A as AuthController
    participant S as AuthService

    F->>F: API call returns 401
    I->>I: Check if refresh in progress (queue)
    I->>A: POST /auth/refresh {refreshToken}
    A->>S: refreshToken(request)
    S->>S: Validate refresh token hash in DB
    S->>S: Check not revoked / not expired
    S->>S: Issue new access token
    S->>S: Rotate refresh token (optional)
    S-->>A: AuthResponse
    A-->>I: New tokens
    I->>I: Retry original request
    I-->>F: Success response
```

## Logout Flow

1. Client sends `POST /auth/logout` with refresh token
2. Server marks refresh token as `revoked = true` in DB
3. Client clears local tokens and AuthContext
4. Access token expires naturally (no server-side blacklist in MVP; add Redis denylist at scale)

## Role-Based Access Control

### Roles

| Role | Capabilities |
|------|-------------|
| `ROLE_USER` | Compare, save, review, basic search |
| `ROLE_PREMIUM` | AI chat, advanced sentiment, price alerts |
| `ROLE_ADMIN` | CRUD products/categories, moderation, analytics |

### Implementation

```java
// SecurityConfig
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/v1/auth/**").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
    .requestMatchers("/api/v1/analytics/**").hasRole("ADMIN")
    .requestMatchers("/api/v1/ai/chat").hasRole("PREMIUM")
    .anyRequest().authenticated()
)

// Controller
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/analytics/dashboard")
public ResponseEntity<?> dashboard() { ... }
```

## JWT Payload Structure

**Access Token:**
```json
{
  "sub": "user-uuid",
  "email": "user@example.com",
  "roles": ["ROLE_USER"],
  "iat": 1717934400,
  "exp": 1717935300,
  "type": "access"
}
```

**Refresh Token:**
```json
{
  "sub": "user-uuid",
  "jti": "token-uuid",
  "iat": 1717934400,
  "exp": 1718539200,
  "type": "refresh"
}
```

## Security Checklist

- [ ] JWT secret ≥ 256 bits, stored in env/secrets manager
- [ ] HTTPS only in production
- [ ] CORS: allow only frontend origin
- [ ] Password policy: min 8 chars, uppercase, lowercase, digit, special
- [ ] Rate limit: 5 login attempts / minute / IP
- [ ] Refresh token stored hashed in DB (SHA-256)
- [ ] `@Valid` on all request DTOs
- [ ] No sensitive data in JWT payload beyond id/email/roles
- [ ] Actuator endpoints secured or disabled in prod

## Frontend Auth Implementation

```
AuthContext
├── user: User | null
├── accessToken: string | null
├── login(credentials)
├── register(data)
├── logout()
└── isAuthenticated: boolean

Axios Interceptor
├── Request: attach Authorization header
└── Response: on 401 → refresh → retry queue

ProtectedRoute
├── checks isAuthenticated
├── optional requiredRole prop
└── redirect to /login if unauthorized
```

## Password Reset (Phase 1.5)

1. `POST /auth/forgot-password` → generate token, send email (async)
2. User clicks link: `/reset-password?token=xxx`
3. `POST /auth/reset-password` → validate token, update password, revoke all refresh tokens
