CREATE TABLE invoice (
  id INT PRIMARY KEY NOT NULL,
  client VARCHAR(50),
  created TIMESTAMP,
  amount DECIMAL(12, 2),
  tax_codes VARCHAR ARRAY[8],
  paid char(1) CHECK (paid IN ('Y', 'N'))
);

INSERT INTO invoice (id, client, created, amount, tax_codes, paid) VALUES
  (10, 'Los Alamos', '2022-04-10', 105.60, ARRAY ['1001'], 'Y'),
  (11, 'Blue Store', '2023-12-07', 230.05, ARRAY ['2077', '2078', '2301'], 'N'),
  (12, 'Ultima Games', '2024-09-12', 140.49, ARRAY ['1001', '1002'], 'Y'),
  (13, 'Daily Bagel', '2025-05-28', 49.99, null, 'N');

