create table product (
  id int,
  type varchar(6),
  shipping int
);

create table branch (
  id int,
  region varchar(10),
  is_vip boolean
);

create table account (
  id int,
  branch_id int,
  parent_id int
);

create table invoice (
  id int,
  account_id int,
  amount int,
  order_date date,
  type varchar(10),
  unpaid_balance int,
  status varchar(10)
);

create table invoice_line (
  invoice_id int,
  product_id int,
  line_total int
);

create table payment (
  payment_date date,
  invoice_id int,
  amount int
);

