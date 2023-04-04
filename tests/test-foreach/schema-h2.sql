
drop schema if exists schema2 cascade;
create schema schema2;
drop table if exists schema2.account;
drop view if exists schema2.v2;

drop schema if exists schema3 cascade;
create schema schema3;
drop table if exists schema3.company;
drop table if exists schema3.code;
drop view if exists schema3.v3;
drop table if exists employee;
drop table if exists branch;

create table branch (
  branch_id int primary key auto_increment,
  branch_name varchar(20) not null
);

insert into branch (branch_id, branch_name) values (100, 'South');
insert into branch (branch_id, branch_name) values (101, 'North');
insert into branch (branch_id, branch_name) values (102, 'West');
insert into branch (branch_id, branch_name) values (103, 'East');

create table employee (
  id int primary key auto_increment,
  name varchar(20) not null,
  branch_id int not null references branch (branch_id)
);

insert into employee (id, name, branch_id) values (45, 'Anne', 100);
insert into employee (id, name, branch_id) values (123, 'Alice', 102);
insert into employee (id, name, branch_id) values (6097, 'Steve', 100);
insert into employee (id, name, branch_id) values (599, 'Mary', 101);
insert into employee (id, name, branch_id) values (6098, 'John', 103);


create table schema2.account (
  id int primary key not null,
  name varchar(20)
--  branch_code int references public.branch (branch_id)
);

insert into schema2.account (id, name) values (123, 'Account 123');
insert into schema2.account (id, name) values (124, 'Account 124');

create view v2 as select * from schema2.account;

create table schema3.company (
  id int primary key not null,
  name varchar(100)
);

insert into schema3.company (id, name) values (12, 'Coca Cola');
insert into schema3.company (id, name) values (45, 'Acme');

create table schema3.code (
  id int primary key not null,
  name varchar(10)
);

create view v3 as select * from schema3.code;


