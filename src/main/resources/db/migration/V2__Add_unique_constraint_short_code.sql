-- Add unique constraint on short_code to prevent duplicates
ALTER TABLE url_mapping ADD CONSTRAINT uk_short_code UNIQUE(short_code);
