drop table if exists branch; 
drop table if exists employee; 

create table branch (
  id int primary key not null,
  name varchar(15) not null,
  type int
);

insert into branch (id, name, type) values
  (1, 'South', 5),
  (2, 'North', 2),
  (3, 'Mountain', 4),
  (4, 'VIP', 4),
  (5, 'West', 6),
  (6, 'East Coast', 6),
  (7, 'Lakeside', 7);

create table employee (
  id int primary key not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  branch_id int -- references branch (id)
);

insert into employee (id, first_name, last_name, branch_id) values
  (101457, 'Anne', 'Smith', 2),
  (609792, 'Steve', 'Locksmith', 6),
  (899288, 'Ronald', 'Kaminkow', 7),
  (134081, 'Alice', 'Badell', 1),
  (207121, 'Julia', 'Whitesmith', 2),
  (610043, 'John', 'Gardener', 4);
