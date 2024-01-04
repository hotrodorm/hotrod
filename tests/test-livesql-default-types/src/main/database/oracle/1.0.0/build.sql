create table branch (
  id number(6) primary key not null,
  "NaMe" varchar2(20),
  region varchar2(10),
  is_vip varchar2(1)
);

insert into branch (id, "NaMe", region) values (101, 'South', 'South');
insert into branch (id, "NaMe", region) values (102, 'East', 'East');

create table invoice (
  id number(6) primary key not null,
  amount number(6),
  branch_id number(6) references branch (id),
  account_id number(6),
  status varchar(6),
  type varchar(6),
  order_date date,
  unpaid_balance number(6)
);

insert into invoice (id, amount, branch_id, account_id, status, unpaid_balance) values (10, 1500, 101, 15, 'PAID', 200);
insert into invoice (id, amount, branch_id, account_id, status, unpaid_balance) values (11, 2500, 101, 15, 'UNPAID', 200);
insert into invoice (id, amount, branch_id, account_id, status, unpaid_balance) values (12, 4000, 102, 15, 'LATE', 200);

-- Types

create table numbers (
  id number(9) primary key not null,
  
-- number, numeric, decimal, dec
  num1 number(2), -- max 38 digits
  num2 number(4), -- max 38 digits
  num3 number(9), -- max 38 digits
  num4 number(18), -- max 38 digits
  num5 number(38), -- max 38 digits
  num6 number(10, 2), -- max 38 digits
  num7 number,
  
  num8 binary_float, -- 32-bit single-precision floating point
  num9 binary_double, -- 64-bit single-precision floating point
  num10 float,
  num11 real,
  num12 double precision,
  
  num20 smallint,
  num21 integer,
  num22 int
);

