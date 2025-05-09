create table t (
  a int,
  b varchar(10)
);

insert into t (a, b) values
  (10, 'abc'),
  (11, 'def'),
  (12, 'ghi'),
  (13, 'jkl'),
  (14, 'mno'),
  (15, 'pqr');
  
create table employee (
  id int primary key not null,
  first_name varchar(20),
  last_name varchar(20),
  hired_on date,
  salary integer,
  dept_no integer,
  active char(1) not null check (active in ('Y', 'N'))
);

insert into employee (id, first_name, last_name, hired_on, salary, dept_no, active) values
  (101, 'Anne', 'Smith', '2021-01-15', 150, 5, 'Y'),
  (102, 'Steve', 'Arunsen', '2022-03-03', 110, 6, 'Y'),
  (103, 'Leyla', 'Martinez', '2023-12-22', 90, 5, 'Y'),
  (104, 'Denis', 'Smith', '2024-04-07', 160, 4, 'N');
  
  