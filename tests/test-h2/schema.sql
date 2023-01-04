drop table if exists employee; 

create table employee (
  id int primary key not null,
  name varchar(20) not null
);

insert into employee (id, name) values (45, 'Anne');
insert into employee (id, name) values (123, 'Alice');
insert into employee (id, name) values (6097, 'Steve');

create table invoice (a int);

create table client (name varchar(10), vip int);

create view vip_client as select * from client where vip = 1;

-- SCHEMA2 ---------------------------------------

create schema schema2;

create table schema2.account (id int, balance int);

-- SCHEMA3 ---------------------------------------

create schema schema3;

create table schema3.task (id int, name varchar(10), due date);

create view schema3.overdue_task as select * from schema3.task where due < current_date();

