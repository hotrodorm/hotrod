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
  parent_id int,
  branch_id int
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

insert into account (id, parent_id, branch_id) values
  (1, null, 100),
  (2, 1, 101),
  (3, 1, 102),
  (4, 3, 103),
  (5, 4, 104);
  
insert into branch (id, region, is_vip) values
  (1, 'N', true),
  (2, 'S', true),
  (3, 'W', false),
  (4, 'E', false),
  (5, 'NE', false),
  (6, 'NW', true),
  (7, 'SE', false);
  
  -- update branch set region = 'x' where id >= 4 and not is_vip
  

