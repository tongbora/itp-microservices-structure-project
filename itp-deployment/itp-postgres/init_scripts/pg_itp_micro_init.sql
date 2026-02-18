-- =========================
-- Role privileges
-- =========================
ALTER ROLE itpusr WITH LOGIN REPLICATION;

-- =========================
-- Databases (CREATE ONCE)
-- =========================
CREATE DATABASE db_customer OWNER itpusr;
CREATE DATABASE db_account OWNER itpusr;
CREATE DATABASE db_transaction OWNER itpusr;
CREATE DATABASE db_transfer OWNER itpusr;
CREATE DATABASE db_payment OWNER itpusr;
CREATE DATABASE db_card OWNER itpusr;
CREATE DATABASE db_product OWNER itpusr;

-- =========================
-- Connect to db_product
-- =========================
\connect db_product

-- =========================
-- Tables
-- =========================
CREATE TABLE IF NOT EXISTS public.products (
                                               id SERIAL PRIMARY KEY,
                                               code VARCHAR(255) NOT NULL,
                                               qty INTEGER,
                                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Required for Debezium CDC
ALTER TABLE public.products REPLICA IDENTITY FULL;

-- =========================
-- Seed data
-- =========================
INSERT INTO public.products (code, qty)
VALUES
    ('D1', 100),
    ('D2', 200),
    ('D3', 300),
    ('D4', 400),
    ('D5', 500),
    ('D6', 600),
    ('D7', 700),
    ('D8', 800),
    ('D9', 900),
    ('D10', 1000)
ON CONFLICT DO NOTHING;

-- =========================
-- Permissions
-- =========================
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO itpusr;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO itpusr;
