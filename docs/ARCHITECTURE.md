# BetterChoice AI — System Architecture

## 1. High-Level Overview

BetterChoice AI is a **modular monolith** that exposes versioned REST APIs to a React SPA. Each business domain is an isolated module with its own controller, service, repository, DTO, entity, mapper, and exception types. Cross-cutting concerns (security, validation, logging, AI client) live in a shared **core** package.

```
┌─────────────────────────────────────────────────────────────────┐
│                     React SPA (Frontend)                        │
│  Pages │ Components │ Hooks │ Services (Axios) │ Context/Store  │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTPS / REST (v1)
┌────────────────────────────▼────────────────────────────────────┐
│              API Gateway Layer (Spring MVC)                     │
│  /api/v1/*  │  GlobalExceptionHandler  │  RequestValidation     │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                   Security Filter Chain                         │
│  JwtAuthFilter → RoleAuthorization → CORS → RateLimit (future)  │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│              Modular Monolith (Domain Modules)                  │
│  auth │ user │ product │ comparison │ review │ ai │ analytics   │
│  recommendation │ search                                          │
└───────┬─────────────────────────────┬───────────────────────────┘
        │                             │
┌───────▼────────┐           ┌────────▼────────┐
│  PostgreSQL    │           │  Gemini API     │
│  (JPA/Hibernate)│          │  (HTTP Client)  │
└────────────────┘           └─────────────────┘
```

## 2. Architectural Principles

### Modular Monolith

- Single deployable JAR with **clear module boundaries** (package-level).
- Modules communicate via **public service interfaces**, never direct repository access across modules.
- Enables future extraction to microservices without rewrite.

### Clean Architecture Layers (per module)

```
Controller  →  Service (interface + impl)  →  Repository
     ↑              ↑
   DTO/Mappers    Entity
```

| Layer | Responsibility |
|-------|----------------|
| **Controller** | HTTP mapping, input validation (`@Valid`), response shaping |
| **Service** | Business logic, transactions, orchestration |
| **Repository** | Data access (Spring Data JPA) |
| **Entity** | Persistence model |
| **DTO** | API contracts (request/response) |
| **Mapper** | Entity ↔ DTO conversion (MapStruct) |
| **Exception** | Module-specific exceptions + error codes |

### Dependency Rule

Dependencies point **inward**: Controllers depend on Services; Services depend on Repositories. No layer skips another. Shared kernel (`core`) provides infrastructure only.

## 3. Backend Package Structure

```
com.betterchoice
├── BetterChoiceApplication.java
├── core/                              # Shared infrastructure
│   ├── config/                        # Security, JPA, OpenAPI, CORS
│   ├── security/                      # JWT, UserDetails, filters
│   ├── exception/                     # GlobalExceptionHandler, ApiError
│   ├── validation/                    # Custom validators
│   ├── util/                          # DateUtils, StringUtils
│   └── client/                        # GeminiClient, HttpClient config
│
├── module/
│   ├── auth/
│   │   ├── controller/AuthController.java
│   │   ├── service/AuthService.java, AuthServiceImpl.java
│   │   ├── repository/RefreshTokenRepository.java
│   │   ├── dto/{request,response}/
│   │   ├── entity/RefreshToken.java
│   │   ├── mapper/AuthMapper.java
│   │   └── exception/InvalidCredentialsException.java
│   │
│   ├── user/
│   ├── product/
│   ├── comparison/
│   ├── review/
│   ├── ai/
│   ├── analytics/
│   ├── recommendation/
│   └── search/
│
└── shared/                            # Cross-module domain types
    ├── enums/                         # Role, CategoryType, ComparisonStatus
    └── event/                         # Domain events (future)
```

## 4. Frontend Architecture

```
frontend/src/
├── app/                    # App shell, providers, router
├── pages/                  # Route-level components
│   ├── auth/
│   ├── home/
│   ├── search/
│   ├── compare/
│   ├── dashboard/
│   └── profile/
├── components/
│   ├── ui/                 # Button, Input, Card, Modal (design system)
│   ├── layout/             # Navbar, Footer, Sidebar
│   └── features/           # ComparisonTable, ProductCard, AIInsight
├── hooks/                  # useAuth, useComparison, useDebounce
├── services/               # Axios API clients per domain
├── context/                # AuthContext, ThemeContext
├── utils/                  # formatters, validators
├── constants/              # API routes, roles, categories
└── assets/
```

### Frontend Patterns

- **Feature-based folders** for scalability
- **Custom hooks** encapsulate API + state logic
- **Axios interceptors** attach JWT and handle 401 refresh
- **Protected routes** via `RequireAuth` wrapper with role checks
- **Tailwind** utility-first with `@apply` in component classes for consistency

## 5. API Versioning

All endpoints are prefixed with `/api/v1/`. Breaking changes require `/api/v2/`.

```
/api/v1/auth/*
/api/v1/users/*
/api/v1/products/*
/api/v1/comparisons/*
...
```

Version is in URL (not header) for clarity in logs and documentation.

## 6. Module Responsibilities

| Module | Responsibility |
|--------|----------------|
| **auth** | Register, login, logout, refresh token, password reset |
| **user** | Profile CRUD, preferences, saved items |
| **product** | Product catalog, categories, attributes, external source sync |
| **comparison** | Create/run comparisons, save, share, history |
| **review** | User reviews, ratings, moderation queue |
| **ai** | Gemini integration, prompts, insight generation, caching |
| **analytics** | Usage metrics, comparison trends, admin dashboards |
| **recommendation** | Personalized suggestions, collaborative filtering (Phase 2+) |
| **search** | Full-text search, filters, facets, autocomplete |

## 7. Inter-Module Communication

```
comparison.service  →  product.service (fetch items)
comparison.service  →  ai.service (generate insights)
recommendation      →  analytics + user preferences
search              →  product.repository (read-only via service)
```

**Rule:** Module A calls Module B's **Service interface** only.

## 8. Entity Relationship Summary

See [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for full DDL.

```
User ──┬── RefreshToken
       ├── UserPreference
       ├── Comparison (owner)
       ├── Review
       └── SavedComparison

Category ── Product ──┬── ProductAttribute
                      ├── ProductSource (Amazon, Yelp, etc.)
                      └── Review

Comparison ── ComparisonItem ── Product
           └── ComparisonInsight (AI-generated)

SearchQuery (analytics) ── User (optional)
PriceHistory ── Product (Phase 3)
```

## 9. Security Architecture

- **Stateless JWT** (access token 15 min, refresh token 7 days)
- **BCrypt** password hashing (strength 12)
- **Roles:** `ROLE_USER`, `ROLE_PREMIUM`, `ROLE_ADMIN`
- **Method-level security:** `@PreAuthorize("hasRole('ADMIN')")`
- **CORS** restricted to frontend origin in production
- **Rate limiting** on auth and AI endpoints (Phase 2)

## 10. AI Integration Architecture

```
ComparisonService
    └── AiInsightService
            ├── PromptTemplateEngine (category-aware prompts)
            ├── GeminiClient (REST, retry, timeout)
            ├── ResponseParser (structured JSON output)
            └── InsightCache (Redis optional, DB fallback)
```

Gemini receives structured product data + user context and returns:
- Summary comparison
- Pros/cons per item
- Winner recommendation with confidence
- Sentiment summary (Phase 2)

## 11. Scalability Path

| Stage | Approach |
|-------|----------|
| MVP | Single EC2 / Render, managed PostgreSQL |
| Growth | Horizontal app replicas behind ALB, Redis cache |
| Scale | Extract `ai` and `search` modules to separate services |
| Enterprise | Event-driven (Kafka), read replicas, CDN for frontend |

The modular monolith is designed so each `module.*` package can become a microservice with minimal refactoring.

## 12. Observability

- **Logging:** Structured JSON (Logback + Logstash encoder)
- **Metrics:** Micrometer + Prometheus
- **Tracing:** OpenTelemetry (Phase 2)
- **Health:** `/actuator/health`, `/actuator/info`
