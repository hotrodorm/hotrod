create table account (
  id int primary key not null,
  title varchar(20),
  created date,
  balance int
);

insert into account (id, title, created, balance) values
  (107, 'CHK1010', '2023-09-10', 100),
  (108, 'SAV2307', '2024-09-11', 20),
  (109, 'CHK1015', '2025-09-12', 140),
  (110, 'SAV2308', '2025-09-15', 45);

