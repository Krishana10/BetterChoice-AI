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

- Java 21+ (JDK 25 supported with Lombok 1.18.40+)
- Node.js 20+, npm
- PostgreSQL 16
- Docker (optional)
- Maven is **optional** — the backend includes `mvnw.cmd` (Maven Wrapper)

### Backend

```powershell
cd backend

# Set JAVA_HOME if not configured (adjust path to your JDK)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"

# Use mvnw.cmd — no global Maven install needed
.\mvnw.cmd clean compile -DskipTests

# Quote -D flags in PowerShell (otherwise it splits on the hyphen)
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Copy `src/main/resources/application-dev.yml.example` to `application-dev.yml` if missing.

**Docker Postgres** uses password `postgres` — run with:

```bash
# PowerShell
$env:SPRING_DATASOURCE_PASSWORD="postgres"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**`mvn` not recognized?** Use `.\mvnw.cmd` instead of `mvn` (same flags).

**Troubleshooting `BUILD FAILURE` on `spring-boot:run`:**
- `TypeTag :: UNKNOWN` → upgrade Lombok (already 1.18.40 in pom) or use JDK 21
- `password authentication failed` → set `SPRING_DATASOURCE_PASSWORD` to match your Postgres
- `Connection refused` → start Postgres (`docker compose up -d` in `docker/`)
- `Migration checksum mismatch for migration version 2` → dev profile auto-repairs on startup (`FlywayDevConfig`); or reset DB: `DROP DATABASE betterchoice; CREATE DATABASE betterchoice;`

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

## License Krishna kumar

Proprietary — BetterChoice AI
