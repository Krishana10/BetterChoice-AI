# Backend Module Placeholders
# Each module follows: controller / service / repository / dto / entity / mapper / exception

backend/src/main/java/com/betterchoice/
├── BetterChoiceApplication.java
├── core/
│   ├── config/SecurityConfig.java          # TODO: JWT filter chain
│   ├── security/JwtProvider.java           # TODO: Token generation/validation
│   ├── client/GeminiClient.java            # TODO: Gemini API integration
│   ├── dto/ApiResponse.java
│   └── exception/
├── shared/enums/
└── module/
    ├── auth/       ✓ scaffolded
    ├── user/       ✓ entity + dto
    ├── product/    TODO
    ├── comparison/ TODO
    ├── review/     TODO
    ├── ai/         TODO
    ├── analytics/  TODO
    ├── recommendation/ TODO
    └── search/     TODO

Copy database/migrations/V1__initial_schema.sql to:
  backend/src/main/resources/db/migration/V1__initial_schema.sql
