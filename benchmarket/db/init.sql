-- Création de l'utilisateur benchmark avec mot de passe
CREATE USER benchmark WITH PASSWORD 'benchmark123';

-- Créer la base de données benchmark si elle n'existe pas
CREATE DATABASE benchmark OWNER benchmark;

-- Se connecter à la base de données benchmark
\c benchmark

-- Création des tables
CREATE TABLE category (
  id            BIGSERIAL PRIMARY KEY,
  code          VARCHAR(32) UNIQUE NOT NULL,
  name          VARCHAR(128)       NOT NULL,
  updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE item (
  id            BIGSERIAL PRIMARY KEY,
  sku           VARCHAR(64) UNIQUE NOT NULL,
  name          VARCHAR(128)       NOT NULL,
  price         NUMERIC(10,2)      NOT NULL,
  stock         INT                NOT NULL,
  category_id   BIGINT             NOT NULL REFERENCES category(id),
  description   TEXT,
  updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Index
CREATE INDEX idx_item_category   ON item(category_id);
CREATE INDEX idx_item_updated_at ON item(updated_at);

-- Donner les permissions à l'utilisateur benchmark
GRANT ALL PRIVILEGES ON TABLE category TO benchmark;
GRANT ALL PRIVILEGES ON TABLE item TO benchmark;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO benchmark;

-- Création des tables
CREATE TABLE category (
  id            BIGSERIAL PRIMARY KEY,
  code          VARCHAR(32) UNIQUE NOT NULL,
  name          VARCHAR(128)       NOT NULL,
  updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE item (
  id            BIGSERIAL PRIMARY KEY,
  sku           VARCHAR(64) UNIQUE NOT NULL,
  name          VARCHAR(128)       NOT NULL,
  price         NUMERIC(10,2)      NOT NULL,
  stock         INT                NOT NULL,
  category_id   BIGINT             NOT NULL REFERENCES category(id),
  description   TEXT,
  updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Index
CREATE INDEX idx_item_category   ON item(category_id);
CREATE INDEX idx_item_updated_at ON item(updated_at);

-- Donner les permissions à l'utilisateur benchmark
GRANT ALL PRIVILEGES ON TABLE category TO benchmark;
GRANT ALL PRIVILEGES ON TABLE item TO benchmark;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO benchmark;

-- Insertion des données de test
-- 2000 catégories
INSERT INTO category (code, name)
SELECT
  'CAT' || LPAD(ROW_NUMBER() OVER ()::TEXT, 4, '0'),
  'Category ' || ROW_NUMBER() OVER ()
FROM generate_series(1, 2000);

-- 100 000 items (~50 par catégorie)
INSERT INTO item (sku, name, price, stock, category_id, description)
SELECT
  'SKU' || LPAD(ROW_NUMBER() OVER ()::TEXT, 6, '0'),
  'Item ' || ROW_NUMBER() OVER (),
  (RANDOM() * 1000)::NUMERIC(10,2),
  (RANDOM() * 1000)::INT,
  ((ROW_NUMBER() OVER () - 1) % 2000) + 1,
  CASE WHEN RANDOM() < 0.1 THEN 'Heavy payload description for performance testing with larger data size' ELSE NULL END
FROM generate_series(1, 100000);


