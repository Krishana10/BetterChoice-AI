# BetterChoice AI — Best Practices & Deployment

## Best Practices

### Backend (Java / Spring Boot)

#### Code Organization
- One public service interface per module; hide impl in same package
- Use **MapStruct** for entity ↔ DTO mapping (compile-time, no reflection)
- Keep controllers thin — no business logic
- Use `@Transactional` on service methods, read-only for queries
- Return `ResponseEntity<ApiResponse<T>>` consistently

#### Validation
```java
public record RegisterRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) @Pattern(...) String password,
    @NotBlank @Size(max = 150) String fullName
) {}
```

#### Exception Handling
- Module exceptions extend `BusinessException` with error codes
- `GlobalExceptionHandler` maps to HTTP status + `ApiError`
- Never expose stack traces in production

#### Security
- Never log passwords or tokens
- Use `@PreAuthorize` over inline role checks
- Parameterize all JPA queries (no string concatenation)
- Validate file uploads if added later

#### Testing
```
Unit tests     → Service layer (mock repositories)
Integration    → @SpringBootTest + Testcontainers PostgreSQL
Controller     → @WebMvcTest + MockMvc
Coverage target → 80% on services, 60% overall
```

#### Database
- Flyway for migrations (never `ddl-auto=update` in prod)
- Index foreign keys and frequently filtered columns
- Use `@Version` for optimistic locking on hot entities
- Soft delete via `active` flag where audit trail matters

---

### Frontend (React)

#### Structure
- Colocate feature components under `components/features/`
- API calls only in `services/` — never in components directly
- Custom hooks return `{ data, loading, error, refetch }`

#### State Management
- **AuthContext** for global auth state
- **Local state** for forms and UI
- Consider **TanStack Query** for server state caching (Phase 2)

#### Performance
- Lazy load routes: `React.lazy(() => import('./pages/Compare'))`
- Debounce search input (300ms)
- Virtualize long product lists
- Optimize images (WebP, lazy loading)

#### Accessibility
- Semantic HTML, ARIA labels on interactive elements
- Keyboard navigation for comparison table
- Color contrast WCAG AA compliant

---

### AI Integration

- **Prompt versioning:** Store prompt template version with each insight
- **Structured output:** Request JSON mode from Gemini; validate with Jackson
- **Fallback:** Return cached insight or graceful "AI unavailable" message
- **Cost control:** Cache insights by product ID set hash; rate limit per user
- **Timeout:** 30s max; async job for long analyses (Phase 3)

```java
// Cache key example
String cacheKey = SHA256(sortedProductIds + categoryType + promptVersion);
```

---

### API Design

- Nouns for resources, verbs for actions (`POST /comparisons/{id}/run`)
- Consistent pagination, sorting, filtering query params
- Idempotent PUT for updates; POST for creates
- Use `201 Created` with `Location` header
- Deprecate endpoints via `Sunset` header before removal

---

### Git Workflow

```
main          → production
develop       → integration
feature/*     → new features
fix/*         → bug fixes
```

- Conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`
- PR required for merge to `develop`
- Squash merge to `main` for releases

---

## Deployment Strategy

### Environment Tiers

| Environment | Purpose | URL Pattern |
|-------------|---------|-------------|
| Local | Development | localhost:5173 / :8080 |
| Dev | Shared testing | dev.betterchoice.ai |
| Staging | Pre-prod validation | staging.betterchoice.ai |
| Production | Live users | betterchoice.ai |

### Recommended Stack (Startup / Portfolio)

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Vercel    │     │ Render/Railway│     │  Neon/Supabase  │
│  (Frontend) │────▶│  (Backend JAR)│────▶│  (PostgreSQL)   │
└─────────────┘     └──────────────┘     └─────────────────┘
                           │
                    ┌──────▼──────┐
                    │ Gemini API  │
                    └─────────────┘
```

**Cost-effective for MVP:** ~$0–25/month on free tiers.

### Docker Production

```yaml
# docker-compose.prod.yml (conceptual)
services:
  backend:
    image: betterchoice/backend:latest
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DATABASE_URL: ${DATABASE_URL}
      JWT_SECRET: ${JWT_SECRET}
      GEMINI_API_KEY: ${GEMINI_API_KEY}
    ports: ["8080:8080"]
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]

  frontend:
    image: betterchoice/frontend:latest
    ports: ["80:80"]
```

### CI/CD Pipeline (GitHub Actions)

```yaml
# On pull request:
1. Backend: mvn verify (unit + integration tests)
2. Frontend: npm ci && npm run lint && npm run build
3. Optional: SonarCloud quality gate

# On merge to main:
1. Build Docker images
2. Push to container registry (GHCR)
3. Deploy backend to Render/Railway
4. Deploy frontend to Vercel
5. Run smoke tests against staging
6. Promote to production (manual approval)
```

### Environment Variables

| Variable | Where | Description |
|----------|-------|-------------|
| `DATABASE_URL` | Backend | PostgreSQL JDBC URL |
| `JWT_SECRET` | Backend | 256-bit signing key |
| `JWT_ACCESS_EXPIRY` | Backend | Default 900 (seconds) |
| `JWT_REFRESH_EXPIRY` | Backend | Default 604800 |
| `GEMINI_API_KEY` | Backend | Google AI API key |
| `CORS_ALLOWED_ORIGINS` | Backend | Frontend URL |
| `VITE_API_BASE_URL` | Frontend | Backend API URL |

**Never commit secrets.** Use GitHub Secrets + platform env config.

### Database Migrations in Production

1. Flyway runs on app startup (`spring.flyway.enabled=true`)
2. Backup DB before major migrations
3. Test migrations on staging first
4. Avoid destructive migrations without dual-write period

### Scaling Checklist

| Traffic Level | Actions |
|---------------|---------|
| < 1K users | Single backend instance, managed DB |
| 1K–10K | 2+ backend replicas, connection pooling (HikariCP tuned) |
| 10K–100K | Redis cache, read replica, CDN |
| 100K+ | Extract AI/search services, message queue, auto-scaling |

### Monitoring & Alerts

- **Uptime:** UptimeRobot or Better Stack on `/actuator/health`
- **Errors:** Sentry for backend + frontend
- **Logs:** Structured JSON → CloudWatch / Datadog
- **Alerts:** PagerDuty/Slack on error rate > 1%, latency p99 > 2s

### Backup Strategy

- PostgreSQL: daily automated backups (managed provider)
- Retention: 7 daily, 4 weekly
- Test restore quarterly

### Security Hardening (Production)

- [ ] TLS everywhere (Let's Encrypt / platform-managed)
- [ ] Security headers (HSTS, CSP, X-Frame-Options)
- [ ] Dependabot enabled
- [ ] OWASP dependency check in CI
- [ ] Rate limiting on auth endpoints
- [ ] WAF on CDN (Cloudflare free tier)

---

## Architecture Decision Records (ADR) Template

Store in `docs/adr/`:

```markdown
# ADR-001: Modular Monolith over Microservices

## Status: Accepted

## Context
Early-stage startup needs fast iteration with clear domain boundaries.

## Decision
Single deployable with package-level modules.

## Consequences
+ Simple ops, easy local dev
- Must enforce module boundaries via code review
- Future extraction possible per module
```
