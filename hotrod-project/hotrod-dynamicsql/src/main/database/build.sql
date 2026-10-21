create table employee (
  id int primary key not null,
  amount int,
  name varchar(20),
  hired date,
  active boolean,
  photo blob,
  region geometry
);

insert into employee (id, amount, name, hired, active, photo, region) values
  (1, 150, 'Anne', date '2026-01-01', true, null, null),
  (2, 250, 'Alice', date '2026-03-15', false, null, null),
  (3, 225, 'Jamal', date '2025-07-22', true, null, null);

