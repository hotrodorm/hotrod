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

