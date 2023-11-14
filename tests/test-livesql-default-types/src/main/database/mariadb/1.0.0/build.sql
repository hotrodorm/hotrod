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


