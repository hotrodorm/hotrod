create table branch (
  id int primary key not null,
  name varchar(20)
);

insert into branch (id, name) values (101, 'South'), (102, 'East');

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id)
);

insert into invoice (id, amount, branch_id) values (10, 1500, 101), (11, 2500, 101), (12, 4000, 102);


