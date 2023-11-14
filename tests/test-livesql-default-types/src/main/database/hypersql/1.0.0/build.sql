
create table branch (
  id int,
  region varchar(10),
  is_vip boolean
);

create table invoice (
  id int,
  account_id int,
  amount int,
  branch_id int,
  order_date date,
  type varchar(10),
  unpaid_balance int,
  status varchar(10)
);

create table account (
  id int,
  parent_id int,
  branch_id int
);