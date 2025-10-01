CREATE TABLE account (
  id INT PRIMARY KEY NOT NULL, -- STATIC_DIALECT_RULE
  balance DECIMAL(12, 2),      -- STATIC_DESIGNATED
  segment TINYINT              -- STATIC_LAYER_RULE
);

INSERT INTO account (id, balance, segment) VALUES
  (3, 215.53, 18),
  (4, 101.01, 14),
  (5, 450.25, 15);

CREATE TABLE invoice (
  id INT PRIMARY KEY NOT NULL,            -- STATIC_DIALECT_RULE
  created TIMESTAMP,                      -- STATIC_LAYER_RULE (type)
  invoice_tax_codes VARCHAR ARRAY[8],     -- STATIC_LAYER_RULE (converter)
  amount DECIMAL(12, 2),                  -- STATIC_DESIGNATED (type)
  paid char(1) CHECK (paid IN ('Y', 'N')) -- STATIC_DESIGNATED (converter)
);

INSERT INTO invoice (id, created, invoice_tax_codes, amount, paid) VALUES
  (10, '2022-04-10', ARRAY ['1001'], 105.60, 'Y'),
  (11, '2023-12-07', ARRAY ['2077', '2078', '2301'], 230.05, 'N'),
  (12, '2024-09-12', ARRAY ['1001', '1002'], 140.49, 'Y'),
  (13, '2025-05-28', null, 49.99, 'N');

/*
select
  id,                -- STATIC_DIALECT_RULE
  created,           -- STATIC_LAYER_RULE (type)
  invoice_tax_codes, -- STATIC_LAYER_RULE (converter)
  amount * 1.3 as "AG1" .type(Double) -- RUNTIME_DESIGNATED
  EXTRACT(DAY FROM created) as "dom" -- RUNTIME_TYPESOLVER_RULE
  CASE WHEN paid THEN amount ELSE 0 END as "paidAmount" -- RUNTIME_DIALECT_RULE
  DATEDIFF(DAY, created, CURRENT_DATE) as "age" -- RUNTIME_JDBC_DRIVER_DEFAULT
from invoice
*/


























