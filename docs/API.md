# BetterChoice AI — API Reference

Base URL: `https://api.betterchoice.ai/api/v1`  
Local: `http://localhost:8080/api/v1`

All responses use a consistent envelope:

```json
{
  "success": true,
  "data": { },
  "message": "Optional message",
  "timestamp": "2026-06-09T12:00:00Z"
}
```

Errors:

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Email is required",
    "details": [{ "field": "email", "message": "must not be blank" }]
  },
  "timestamp": "2026-06-09T12:00:00Z"
}
```

---

## Auth Module — `/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/register` | Public | Register new user |
| POST | `/auth/login` | Public | Login, returns access + refresh tokens |
| POST | `/auth/refresh` | Public | Exchange refresh token for new access token |
| POST | `/auth/logout` | User | Revoke refresh token |
| POST | `/auth/forgot-password` | Public | Send password reset email |
| POST | `/auth/reset-password` | Public | Reset password with token |

### POST `/auth/register`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "fullName": "Jane Doe"
}
```

**Response (201):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJ...",
    "refreshToken": "eyJ...",
    "expiresIn": 900,
    "user": {
      "id": "uuid",
      "email": "user@example.com",
      "fullName": "Jane Doe",
      "role": "ROLE_USER"
    }
  }
}
```

### POST `/auth/login`

**Request:**
```json
{ "email": "user@example.com", "password": "SecurePass123!" }
```

---

## User Module — `/users`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/users/me` | User | Get current user profile |
| PUT | `/users/me` | User | Update profile |
| GET | `/users/me/preferences` | User | Get preferences |
| PUT | `/users/me/preferences` | User | Update preferences |
| GET | `/users` | Admin | List all users (paginated) |
| GET | `/users/{id}` | Admin | Get user by ID |
| PATCH | `/users/{id}/role` | Admin | Change user role |
| DELETE | `/users/{id}` | Admin | Soft-delete user |

---

## Product Module — `/products`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/products` | Public | List products (paginated, filterable) |
| GET | `/products/{id}` | Public | Get product detail |
| GET | `/products/slug/{slug}` | Public | Get product by slug |
| POST | `/products` | Admin | Create product |
| PUT | `/products/{id}` | Admin | Update product |
| DELETE | `/products/{id}` | Admin | Deactivate product |
| GET | `/products/{id}/attributes` | Public | Get product attributes |
| GET | `/products/{id}/sources` | Public | Get external platform listings |

**Query params (GET `/products`):**
- `page`, `size`, `sort`
- `categoryId`, `categoryType`
- `minPrice`, `maxPrice`
- `search` (name/description)

---

## Category Module — `/categories`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/categories` | Public | List categories |
| GET | `/categories/{id}` | Public | Get category |
| POST | `/categories` | Admin | Create category |
| PUT | `/categories/{id}` | Admin | Update category |

---

## Comparison Module — `/comparisons`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/comparisons` | User | Create comparison |
| GET | `/comparisons` | User | List user's comparisons |
| GET | `/comparisons/{id}` | User/Public* | Get comparison detail |
| PUT | `/comparisons/{id}` | User | Update comparison |
| DELETE | `/comparisons/{id}` | User | Delete comparison |
| POST | `/comparisons/{id}/items` | User | Add product to comparison |
| DELETE | `/comparisons/{id}/items/{itemId}` | User | Remove item |
| POST | `/comparisons/{id}/run` | User | Execute comparison + AI insights |
| GET | `/comparisons/{id}/insights` | User/Public* | Get AI insights |
| POST | `/comparisons/{id}/save` | User | Save to favorites |
| DELETE | `/comparisons/{id}/save` | User | Unsave |
| GET | `/comparisons/shared/{shareToken}` | Public | View shared comparison |

*Public if `isPublic=true`

### POST `/comparisons`

**Request:**
```json
{
  "title": "Best Budget Laptops 2026",
  "categoryType": "PRODUCT",
  "productIds": ["uuid1", "uuid2", "uuid3"]
}
```

### POST `/comparisons/{id}/run`

Triggers AI analysis. Returns comparison with insights.

---

## Review Module — `/reviews`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/products/{productId}/reviews` | Public | List reviews |
| POST | `/products/{productId}/reviews` | User | Create review |
| PUT | `/reviews/{id}` | User | Update own review |
| DELETE | `/reviews/{id}` | User | Delete own review |
| GET | `/reviews/pending` | Admin | Moderation queue |
| PATCH | `/reviews/{id}/moderate` | Admin | Approve/reject review |

---

## Search Module — `/search`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/search` | Public | Full-text search |
| GET | `/search/autocomplete` | Public | Suggestions |
| GET | `/search/facets` | Public | Filter facets for category |

**Query params:**
- `q` — search query
- `categoryType`
- `filters` — JSON encoded filters
- `page`, `size`

---

## AI Module — `/ai`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/ai/insights/compare` | User | On-demand comparison insight |
| POST | `/ai/sentiment` | Premium | Sentiment analysis (Phase 2) |
| POST | `/ai/detect-fake-review` | Admin | Fake review detection (Phase 2) |
| POST | `/ai/chat` | Premium | AI assistant chat (Phase 3) |

### POST `/ai/insights/compare`

**Request:**
```json
{
  "productIds": ["uuid1", "uuid2"],
  "categoryType": "PRODUCT",
  "userContext": "Looking for a laptop under $800 for coding"
}
```

---

## Recommendation Module — `/recommendations`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/recommendations/for-you` | User | Personalized recommendations |
| GET | `/recommendations/similar/{productId}` | Public | Similar products |
| GET | `/recommendations/trending` | Public | Trending comparisons |

---

## Analytics Module — `/analytics`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/analytics/dashboard` | Admin | Admin dashboard metrics |
| GET | `/analytics/comparisons/trends` | Admin | Comparison trends |
| GET | `/analytics/search/top-queries` | Admin | Top search queries |
| GET | `/analytics/users/growth` | Admin | User growth over time |

---

## HTTP Status Codes

| Code | Usage |
|------|-------|
| 200 | Success |
| 201 | Created |
| 204 | No content (delete) |
| 400 | Validation error |
| 401 | Unauthorized |
| 403 | Forbidden (role) |
| 404 | Not found |
| 409 | Conflict (duplicate email, etc.) |
| 429 | Rate limited |
| 500 | Internal error |

## Pagination

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

Query: `?page=0&size=20&sort=createdAt,desc`

## Authorization Header

```
Authorization: Bearer <access_token>
```
