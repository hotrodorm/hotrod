CREATE TABLE invoice (
  amount DECIMAL(12, 2),                        -- STATIC_DESIGNATED (type)
  status INT,                                   -- STATIC_DESIGNATED (converter)
  created TIMESTAMP,                            -- STATIC_TYPESOLVER_RULE (type)
  active CHAR(1) CHECK (active IN ('Y', 'N') ), -- STATIC_TYPESOLVER_RULE (converter)
  category DECIMAL(4)                           -- STATIC_DIALECT_RULE
);

INSERT INTO invoice (amount, status, created, active, category) VALUES
  (110.05, 3, '2022-09-14 12:30:21', 'Y', 1015),
  (51.99,  2, '2023-07-22 14:31:35', 'N', 2001),
  (480.14, 1, '2024-02-03 07:32:23', 'Y', 1018),
  (224.01, 3, '2025-10-18 20:33:04', 'N', 1023);
  
