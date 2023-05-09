create table branch (
  id number(6) primary key not null,
  "NaMe" varchar2(20)
);

insert into branch (id, "NaMe") values (101, 'South');
insert into branch (id, "NaMe") values (102, 'East');

create table invoice (
  id number(6) primary key not null,
  amount number(6),
  branch_id number(6) references branch (id)
);

insert into invoice (id, amount, branch_id) values (10, 1500, 101);
insert into invoice (id, amount, branch_id) values (11, 2500, 101);
insert into invoice (id, amount, branch_id) values (12, 4000, 102);

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

