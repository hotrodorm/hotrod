DROP TABLE IF EXISTS "Order Type α";

CREATE TABLE "Order Type α" ( -- the table name has mixed-case letters, spaces, and a greek letter
  id int primary key,
  "case" VARCHAR(200), -- this column name is a reserved word in PostgreSQL
  AGRU_NOMBRE VARCHAR(20)
);

INSERT INTO "Order Type α" (id, "case") VALUES
  (1, 'Type Alpha'),
  (2, 'Beta'),
  (3, 'Gamma'),
  (4, 'Base');
