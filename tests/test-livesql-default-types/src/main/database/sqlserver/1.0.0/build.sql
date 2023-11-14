create table branch (
  id int primary key not null,
  [NaMe] varchar(20),
  region varchar(10),
  is_vip int
);

insert into branch (id, [NaMe], region) values (101, 'SOUTH', 'SOUTH'), (102, 'East', 'EAST');

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id),
  status varchar(10),
  order_date date,
  unpaid_balance int,
  account_id int,
  type varchar(10)
);

insert into invoice (id, amount, branch_id, status) values (10, 1500, 101, 'ABC'), (11, 2500, 101, 'DEF'), (12, 4000, 102, 'GHI');



