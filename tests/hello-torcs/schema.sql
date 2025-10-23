create table employee (
  id int primary key not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  branch_id int
);

insert into employee (id, first_name, last_name, branch_id) values
  (101457, 'Anne', 'Smith', 2),
  (609792, 'Steve', 'Locksmith', 6),
  (899288, 'Ronald', 'Kaminkow', 7),
  (134081, 'Alice', 'Badell', 1),
  (207121, 'Julia', 'Whitesmith', 2),
  (610043, 'John', 'Gardener', 4);
