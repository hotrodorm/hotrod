create table chars (
  cha1 char(10), -- max 1GB
  cha2 varchar(10), -- max 1GB
  cha3 text -- max 1GB
);

create table numbers (
  int1 smallint, -- -32768 to +32767
  int2 integer,  -- -2147483648 to +2147483647
  int3 bigint,   -- -9223372036854775808 to +9223372036854775807
  int4 smallserial,
  int5 serial,
  int6 bigserial,
  int_total_amount integer,
  columns int,
  dec1 decimal(12,2), -- up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
  dec2 numeric(12,2), -- up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
  dec3 decimal(2),
  dec4 decimal(4),
  dec5 decimal(8),
  dec6 decimal(18),
  dec7 decimal(100),
  dec_total_amount decimal(4),
  flo1 real,            -- 6 decimal digits precision
  flo2 double precision, -- 15 decimal digits precision
  flo_total_amount real
--  mon1 money -- fixed precision: 8 bytes, using by default 2 decimal places.
);

create table dates (
  id int not null,
  dat1 date, 
  ts1 timestamp,
  ts2 timestamp without time zone, -- not tested!
  ts3 timestamp with time zone,
  ts4 timestamptz,                -- not tested!
  ts5 timestamp(6),
  tim1 time, 
  tim2 time without time zone,     -- not tested!
  tim3 time with time zone,
  tim4 timetz,                     -- not tested!
  tim5 time(6),
  ivt1 interval YEAR,
  ivt2 interval MONTH,
  ivt3 interval DAY,
  ivt4 interval HOUR,
  ivt5 interval MINUTE,
  ivt6 interval SECOND,
  ivt7 interval SECOND(5),
  ivt8 interval YEAR TO MONTH,
  ivt9 interval DAY TO HOUR,
  ivt10 interval DAY TO MINUTE,
  ivt11 interval DAY TO SECOND,
  ivt12 interval DAY TO SECOND(5),
  ivt13 interval HOUR TO MINUTE,
  ivt14 interval HOUR TO SECOND,
  ivt15 interval HOUR TO SECOND(5),
  ivt16 interval MINUTE TO SECOND,
  ivt17 interval MINUTE TO SECOND(5),
  primary key (id)
);

create table binaries (
  bin1 bytea, -- this is like a blob
  bol1 boolean
);

create type mood as enum ('sad', 'ok', 'happy');

create type complex as (
  r double precision,
  t double precision
);

create table other (
  -- current_mood mood,
  geo1 point,
  geo2 line,
  geo3 lseg,
  geo4 box,
  geo5 path,
  geo6 polygon,
  geo7 circle,
  net1 cidr, -- net specs
  net2 inet,
  net3 macaddr,
  -- bit1 bit(10),
  -- bit2 bit varying(10),
  uui1 uuid, -- universally unique identifier
  -- xml1 xml, -- (optional support)
  jso1 json,
  jso2 jsonb,
  arr1 integer[],
  arr2 char[][],
  arr3 integer array,
  com1 complex,
  ran1 int4range,
  ran2 int8range,
  ran3 numrange,
  ran4 tsrange,
  ran5 tstzrange,
  ran6 daterange
);  



create table branch (
  id int primary key not null,
  "NaMe" varchar(20),
  region varchar(20),
  is_vip boolean
);

insert into branch (id, "NaMe") values (101, 'South'), (102, 'East');

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


