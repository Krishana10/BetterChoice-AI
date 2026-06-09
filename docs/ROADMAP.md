# BetterChoice AI — Development Roadmap

## Phase 0: Foundation (Week 1–2)

**Goal:** Runnable skeleton with CI, database, and project conventions.

### Backend
- [ ] Initialize Spring Boot 3.x project (Java 21)
- [ ] Configure PostgreSQL + Flyway migrations
- [ ] Set up `core` package: SecurityConfig, JwtProvider, GlobalExceptionHandler
- [ ] Create shared enums, ApiResponse wrapper, pagination DTOs
- [ ] Add OpenAPI/Swagger at `/swagger-ui.html`
- [ ] Configure profiles: `dev`, `test`, `prod`

### Frontend
- [ ] Initialize Vite + React + Tailwind
- [ ] Set up React Router, Axios instance, AuthContext
- [ ] Create layout components (Navbar, Footer)
- [ ] Design system: Button, Input, Card, Spinner, Alert

### DevOps
- [ ] Docker Compose (PostgreSQL + backend + frontend)
- [ ] GitHub Actions: build + test on PR
- [ ] `.env.example` files for both apps

**Deliverable:** Empty app runs locally; health check returns 200.

---

## Phase 1: Core MVP (Week 3–8)

**Goal:** Users can register, search products, compare, and save comparisons.

### Sprint 1 — Auth & User (Week 3–4)
- [ ] User entity, repository, service, mapper
- [ ] Register, login, refresh, logout endpoints
- [ ] JWT filter + role-based access
- [ ] Frontend: Login, Register pages
- [ ] Protected routes + token refresh interceptor
- [ ] Profile page (GET/PUT `/users/me`)

### Sprint 2 — Product Catalog (Week 4–5)
- [ ] Category + Product entities with attributes
- [ ] Product CRUD (admin) + public list/detail APIs
- [ ] Seed data script (sample laptops, restaurants, courses)
- [ ] Frontend: Product list, product detail, category filter
- [ ] Pagination and sorting

### Sprint 3 — Search (Week 5–6)
- [ ] Search service with PostgreSQL full-text / trigram
- [ ] Autocomplete endpoint
- [ ] Frontend: Search bar, results page, filters
- [ ] Log search queries for analytics

### Sprint 4 — Comparison (Week 6–8)
- [ ] Comparison + ComparisonItem entities
- [ ] Create, add/remove items, run comparison
- [ ] Side-by-side comparison UI (responsive table/cards)
- [ ] Save/unsave comparisons
- [ ] Comparison history on dashboard
- [ ] Share comparison via public link

**Phase 1 Exit Criteria:**
- End-to-end: register → search → compare 3 products → save → revisit
- Mobile-responsive UI
- 80%+ test coverage on auth + comparison services

---

## Phase 2: AI Intelligence (Week 9–14)

**Goal:** Gemini-powered insights, sentiment, and review trust.

### Sprint 5 — AI Integration (Week 9–10)
- [ ] GeminiClient with retry, timeout, rate limiting
- [ ] Prompt templates per category type
- [ ] ComparisonInsight entity + caching
- [ ] `POST /comparisons/{id}/run` triggers AI analysis
- [ ] Frontend: AI insight panel (summary, pros/cons, winner)

### Sprint 6 — Reviews (Week 10–12)
- [ ] Review CRUD with moderation workflow
- [ ] Review list on product detail
- [ ] Sentiment analysis via Gemini (PREMIUM)
- [ ] Fake review detection heuristic + AI (ADMIN)
- [ ] Admin moderation dashboard

### Sprint 7 — Recommendations (Week 12–14)
- [ ] Similar products (attribute-based + AI)
- [ ] Trending comparisons from analytics
- [ ] "For you" based on user preferences + history
- [ ] Frontend: recommendation widgets on home/dashboard

**Phase 2 Exit Criteria:**
- AI comparison insights generate in < 5s (cached on repeat)
- Review sentiment scores visible on approved reviews
- Premium role gates AI chat prep work

---

## Phase 3: Platform Scale (Week 15–22)

**Goal:** Multi-category excellence, personalization, price tracking, AI assistant.

### Sprint 8 — Multi-Category (Week 15–17)
- [ ] Category-specific comparison templates (college vs product vs salary)
- [ ] Dynamic attribute columns in comparison UI
- [ ] External source sync jobs (product_sources)
- [ ] Platform badges (Amazon, Yelp, etc.)

### Sprint 9 — Personalization (Week 17–19)
- [ ] User preferences engine
- [ ] Email notifications (price drops, new comparisons)
- [ ] Personalized home feed
- [ ] Comparison templates ("Best X under $Y")

### Sprint 10 — Price Tracking (Week 19–20)
- [ ] PriceHistory entity + scheduled sync
- [ ] Price chart on product detail
- [ ] Alert subscriptions (PREMIUM)

### Sprint 11 — AI Assistant (Week 20–22)
- [ ] Conversational chat with comparison context
- [ ] Streaming responses (SSE)
- [ ] Chat history per user
- [ ] Frontend: floating AI assistant widget

**Phase 3 Exit Criteria:**
- 8 category types fully supported
- Price history charts for products with external sources
- AI assistant answers "Which laptop is best for me?"

---

## Ongoing / Cross-Cutting

| Area | Tasks |
|------|-------|
| Testing | Unit (JUnit 5), integration (Testcontainers), E2E (Playwright) |
| Performance | Query optimization, Redis cache, CDN for static assets |
| Security | OWASP audit, dependency scanning, penetration test |
| Documentation | Keep OpenAPI in sync, architecture decision records (ADRs) |
| Monitoring | Prometheus + Grafana, error tracking (Sentry) |

---

## Milestone Timeline

```
Week 1-2   ████ Phase 0: Foundation
Week 3-8   ████████████ Phase 1: MVP
Week 9-14  ████████████ Phase 2: AI
Week 15-22 ████████████████ Phase 3: Scale
```

## Portfolio Presentation Tips

1. **Demo script:** 3-minute flow showing search → AI comparison → saved dashboard
2. **Architecture diagram** in README (link to docs)
3. **Live demo** on Render/Railway + Vercel
4. **Highlight:** Clean module boundaries, JWT auth, Gemini integration
5. **Metrics slide:** Response times, test coverage, module count
