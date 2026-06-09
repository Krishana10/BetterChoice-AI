-- BetterChoice AI — Initial Schema (Flyway)
-- See database/migrations/V1__initial_schema.sql for canonical copy

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(150) NOT NULL,
    role            VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER',
    enabled         BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified  BOOLEAN NOT NULL DEFAULT FALSE,
    avatar_url      VARCHAR(500),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE refresh_tokens (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash      VARCHAR(255) NOT NULL UNIQUE,
    expires_at      TIMESTAMPTZ NOT NULL,
    revoked         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

CREATE TABLE user_preferences (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    preferred_categories    JSONB NOT NULL DEFAULT '[]',
    budget_min              DECIMAL(12, 2),
    budget_max              DECIMAL(12, 2),
    notification_enabled    BOOLEAN NOT NULL DEFAULT TRUE,
    theme                   VARCHAR(20) NOT NULL DEFAULT 'light',
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    type        VARCHAR(50) NOT NULL,
    icon        VARCHAR(50),
    parent_id   UUID REFERENCES categories(id),
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id     UUID NOT NULL REFERENCES categories(id),
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(255) NOT NULL UNIQUE,
    description     TEXT,
    base_price      DECIMAL(12, 2),
    currency        VARCHAR(3) NOT NULL DEFAULT 'USD',
    image_url       VARCHAR(500),
    brand           VARCHAR(150),
    rating_avg      DECIMAL(3, 2) NOT NULL DEFAULT 0,
    review_count    INT NOT NULL DEFAULT 0,
    metadata        JSONB NOT NULL DEFAULT '{}',
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_name_trgm ON products USING gin(name gin_trgm_ops);
CREATE INDEX idx_products_metadata ON products USING gin(metadata);

CREATE TABLE product_attributes (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id      UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    attribute_key   VARCHAR(100) NOT NULL,
    attribute_value TEXT NOT NULL,
    display_order   INT NOT NULL DEFAULT 0,
    UNIQUE (product_id, attribute_key)
);

CREATE TABLE product_sources (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id      UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    platform        VARCHAR(50) NOT NULL,
    external_id     VARCHAR(255) NOT NULL,
    url             VARCHAR(500),
    price           DECIMAL(12, 2),
    last_synced_at  TIMESTAMPTZ,
    UNIQUE (platform, external_id)
);

CREATE INDEX idx_product_sources_platform ON product_sources(platform, external_id);

CREATE TABLE comparisons (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    title           VARCHAR(255) NOT NULL,
    category_type   VARCHAR(50) NOT NULL,
    status          VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    is_public       BOOLEAN NOT NULL DEFAULT FALSE,
    share_token     VARCHAR(64) UNIQUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_comparisons_user ON comparisons(user_id);

CREATE TABLE comparison_items (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comparison_id   UUID NOT NULL REFERENCES comparisons(id) ON DELETE CASCADE,
    product_id      UUID NOT NULL REFERENCES products(id),
    position        INT NOT NULL,
    notes           TEXT,
    UNIQUE (comparison_id, product_id)
);

CREATE TABLE comparison_insights (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comparison_id   UUID NOT NULL REFERENCES comparisons(id) ON DELETE CASCADE,
    insight_type    VARCHAR(50) NOT NULL,
    content         JSONB NOT NULL,
    model_version   VARCHAR(50),
    tokens_used     INT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saved_comparisons (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    comparison_id   UUID NOT NULL REFERENCES comparisons(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, comparison_id)
);

CREATE TABLE reviews (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id          UUID NOT NULL REFERENCES products(id),
    user_id             UUID NOT NULL REFERENCES users(id),
    rating              SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    title               VARCHAR(200),
    body                TEXT NOT NULL,
    verified_purchase   BOOLEAN NOT NULL DEFAULT FALSE,
    sentiment_score     DECIMAL(4, 3),
    is_fake_flagged     BOOLEAN NOT NULL DEFAULT FALSE,
    moderation_status   VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (product_id, user_id)
);

CREATE INDEX idx_reviews_product ON reviews(product_id);

CREATE TABLE search_queries (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID REFERENCES users(id),
    query_text      VARCHAR(500) NOT NULL,
    category_type   VARCHAR(50),
    result_count    INT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_search_queries_created ON search_queries(created_at DESC);

CREATE TABLE price_history (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id      UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    source_id       UUID REFERENCES product_sources(id),
    price           DECIMAL(12, 2) NOT NULL,
    recorded_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_price_history_product_date ON price_history(product_id, recorded_at DESC);

INSERT INTO categories (name, slug, type, icon, sort_order) VALUES
    ('Electronics', 'electronics', 'PRODUCT', 'cpu', 1),
    ('Laptops', 'laptops', 'PRODUCT', 'laptop', 2),
    ('Restaurants', 'restaurants', 'RESTAURANT', 'utensils', 3),
    ('Online Courses', 'online-courses', 'COURSE', 'book-open', 4),
    ('Colleges', 'colleges', 'COLLEGE', 'graduation-cap', 5);
