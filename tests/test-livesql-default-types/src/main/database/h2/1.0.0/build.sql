create table numbers (
  id int primary key not null,
  int1 int
);

create table chars (
  id int primary key not null
);

create table dates (
  id int primary key not null
);

create table binaries (
  id int primary key not null
);

create table other (
  id int primary key not null
);  

create table branch (
  id int primary key not null,
  "NaMe" varchar(20)
);

insert into branch (id, "NaMe") values (101, 'South'), (102, 'East');

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id)
);

insert into invoice (id, amount, branch_id) values (10, 1500, 101), (11, 2500, 101), (12, 4000, 102);



