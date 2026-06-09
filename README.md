# BetterChoice AI

> AI-powered universal comparison platform — compare products, colleges, restaurants, courses, companies, salaries, hotels, and services with intelligent insights.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | React 18, Tailwind CSS, Axios, React Router v6 |
| Backend | Java 21, Spring Boot 3.x, Spring Security, JWT |
| Database | PostgreSQL 16 |
| AI | Google Gemini API |
| Architecture | Modular Monolith + Clean Architecture |

## Repository Structure

```
BetterChoice AI/
├── backend/                 # Spring Boot modular monolith
├── frontend/                # React SPA
├── database/                # SQL migrations & seed data
├── docs/                    # Architecture & API documentation
├── docker/                  # Docker & compose files
└── .github/workflows/       # CI/CD pipelines
```

## Quick Start

### Prerequisites

- Java 21, Maven 3.9+
- Node.js 20+, npm
- PostgreSQL 16
- Docker (optional)

### Backend

```bash
cd backend
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml
# Set DB credentials, JWT secret, GEMINI_API_KEY
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

### Database

```bash
psql -U postgres -f database/migrations/V1__initial_schema.sql
```

## Documentation

| Document | Description |
|----------|-------------|
| [Architecture](docs/ARCHITECTURE.md) | System design, modules, patterns |
| [Database Schema](docs/DATABASE_SCHEMA.md) | Tables, indexes, relationships |
| [API Reference](docs/API.md) | REST endpoints & contracts |
| [Auth Flow](docs/AUTHENTICATION.md) | JWT, roles, security |
| [Roadmap](docs/ROADMAP.md) | Phased development plan |
| [Deployment](docs/DEPLOYMENT.md) | CI/CD, environments, scaling |

## Development Phases

- **Phase 1** — Auth, product search, comparison, saved comparisons, responsive UI
- **Phase 2** — AI recommendations, sentiment analysis, fake review detection
- **Phase 3** — Multi-category, personalization, price tracking, AI assistant

## License

Proprietary — BetterChoice AI
