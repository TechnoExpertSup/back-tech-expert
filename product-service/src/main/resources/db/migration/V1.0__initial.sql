CREATE TABLE IF NOT EXISTS product_entity (
    product_id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(255) UNIQUE,
    product_price NUMERIC,
    product_description TEXT,
    product_quantity INTEGER NOT NULL
);