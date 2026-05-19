create table employee (
  id int primary key not null,
  first_name varchar(20),
  last_name varchar(20),
  hired_on date,
  active char(1),
  city_id int,
  title varchar(30)  
);

insert into employee (id, first_name, last_name, hired_on, active, city_id, title) values
  (101, 'Peter', 'Smith', '2026-01-12', 'A', 14, 'Medic'),
  (105, 'Marie', 'Jonas', '2021-02-15', 'P', 14, 'Virtual Assistant'),
  (223, 'Anne', 'Smith', '2021-02-15', 'A', 14, 'Secretary'),
  (518, 'Arabella', 'Lawson', '2021-02-15', 'A', 27, 'Virtual Assistant'),
  (640, 'Lucy', 'Barr', '2021-02-15', 'P', 27, 'Clerk'),
  (1077, 'Lucas', 'Santon', '2021-02-15', 'P', 27, 'Virtual Assistant'),
  (1241, 'Peter', 'Arunsen', '2025-09-17', 'A', 30, 'Office Manager');
