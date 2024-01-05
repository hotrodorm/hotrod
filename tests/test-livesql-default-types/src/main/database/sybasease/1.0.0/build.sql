create table branch (
  id int primary key not null,
  [NaMe] varchar(20),
  region varchar(10),
  is_vip int null
)

insert into branch (id, [NaMe], region) values (101, 'SOUTH', 'SOUTH')

insert into branch (id, [NaMe], region) values (102, 'East', 'EAST')

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id),
  status varchar(10),
  order_date date null,
  unpaid_balance int null,
  account_id int null,
  type varchar(10) null
)

insert into invoice (id, amount, branch_id, status) values (10, 1500, 101, 'ABC')

insert into invoice (id, amount, branch_id, status) values (11, 2500, 101, 'DEF') 

insert into invoice (id, amount, branch_id, status) values (12, 4000, 102, 'GHI')



