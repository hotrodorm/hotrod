create table branch (
  id int primary key not null,
  name varchar(20),
  region varchar(20),
  is_vip boolean  
);

insert into branch (id, name) values (101, 'South'), (102, 'East');

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id),
  account_id int,
  unpaid_balance int,
  type varchar(10),
  status varchar(10),
  order_date date  
);

insert into invoice (id, amount, branch_id) values (10, 1500, 101), (11, 2500, 101), (12, 4000, 102);


create table account (
  id int primary key auto_increment,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
);

insert into account (id, name, type, balance) values
  (123, '1010', 'CHK', 100),
  (456, '2055', 'SAV', 200),
  (789, '1072', 'CHK', 500);

-- create sequence seq_account start with 1000;

-- generated keys

create table data (
  name varchar(20)
);

insert into data (name) values ('Alice');
insert into data (name) values ('Anne');
insert into data (name) values ('Alanis');

create table k1 (
  id tinyint AUTO_INCREMENT primary key not null,
  name varchar(20)
);

create table k2 (
  id smallint AUTO_INCREMENT primary key not null,
  name varchar(20)
);

create table k3 (
  id int AUTO_INCREMENT primary key not null,
  name varchar(20)
);

create table k4 (
  id bigint AUTO_INCREMENT primary key not null,
  name varchar(20)
);


create table t5 (
  id bigint primary key,
  name varchar(20)
);
