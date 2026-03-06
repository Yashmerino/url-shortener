CREATE TABLE url_mapping (
    id BIGSERIAL PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_code TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE
);