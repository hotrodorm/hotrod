create table log (
  recorded_at timestamp,
  notes varchar(100)
);

create table parameters (
  system integer,
  level integer,
  name varchar(50),
  value varchar(100)
);

create index param_i1 on parameters (system, level);
create index param_i2 on parameters (name);

create table properties (
  application varchar(20),
  name varchar(50) not null,
  prop_value varchar(80),
  constraint props_name_uc unique (name)
);

create index properties_i1 on properties (application);

create table config_values (
  node integer not null,
  cell integer not null,
  name varchar(20) not null,
  verbatim varchar(50),
  constraint cfgval_uc1 unique (node, cell),
  constraint cfgval_uc2 unique (name)
);

-- must be executed separately to produce multiple identical indexes.
 
alter table config_values add constraint cfgval_uc10 unique (name);
alter table config_values add constraint cfgval_uc11 unique (name);

create table account (
  id serial not null,
  name varchar(100) not null,
  type varchar(10) not null,
  current_balance integer not null,
  created_on timestamp not null,
  active int not null,
  primary key (id),
  constraint account_name_uc unique (name)
);

create table state_branch (
  id integer not null,
  name varchar(30) not null,
  primary key (id)
);

create table federal_branch (
  id integer not null,
  name varchar(30) not null,
  primary key (id)
);

create table transaction (
  account_id integer not null,
  seq_id serial not null,
  time varchar(16) not null,
  amount integer not null,
  fed_branch_id bigint,
  primary key (seq_id),
  constraint tx_account_id_time unique (account_id, time),
  constraint fk_tx_account1 foreign key (account_id) 
    references account (id),
  constraint fk_tx_account2 foreign key (account_id) 
    references account (id),
  constraint fk_tx_account3 foreign key (account_id) 
    references account (id),
  constraint fk_tx_fed_branch foreign key (fed_branch_id)
    references federal_branch (id)
);

create table client (
  id integer not null,
  national_id integer not null,
  name varchar(40) not null,
  prop_name varchar(20) not null,
  referrer_id integer,
  friend_id integer,
  group_account_id integer,
  branch_id integer,
  constraint client_nat_id unique (national_id),
  primary key (id),
  constraint fk_client_properties foreign key (prop_name)  -- to other UI
    references properties (name),
  constraint fk_client_referrer foreign key (referrer_id) -- to self PK
    references client (id),
  constraint fk_client_friend foreign key (friend_id) -- to self UI
    references client (national_id),
  constraint fk_client_gaccount foreign key (group_account_id) -- to other PK
    references account (id),
  constraint fk_client_st_branch foreign key (branch_id) -- dual fk #1
    references state_branch (id),
  constraint fk_client_fed_branch foreign key (branch_id) -- dual fk #2
    references federal_branch (id)
);

create sequence seq_agent;

create table agent (
  id integer not null,
  name varchar(40) not null,
  client_id bigint,
  primary key (id),
  constraint fk_agent_client1 foreign key (client_id) references client (id),
  constraint fk_agent_client2 foreign key (client_id) references client (id),
  constraint fk_agent_client3 foreign key (client_id) references client (id)
);

create table quadrant (
  region integer not null,
  area integer not null,
  caption varchar(20),
  primary key (region, area)
);

create table codes (
  id serial not null,
  account integer not null,
  version_name integer not null,
  account_version integer not null,
  name integer not null,
  constraint fk_codes_q1 foreign key (account, version_name) 
    references quadrant (region, area),
  constraint fk_codes_q2 foreign key (account_version, name) 
    references quadrant (region, area),
  constraint cd_q1 unique (account, version_name),
  constraint cd_q2 unique (account_version, name)
);

create table application_config (
  config_id integer not null,
  config_name varchar(150),
  config_value varchar(250)
);

create view tx_branch
    (account_id, branch_id, branch_name, amount) as
  select t.account_id, b.id, b.name, t.amount 
    from transaction t, federal_branch b 
    where t.fed_branch_id = b.id
    order by t.amount;
  
create table vehicle_type (
  id int primary key not null,
  description varchar(20)
);

create table vehicle (
  id serial not null,
  name varchar(40) not null,
  mileage integer not null,
  version_number smallint not null,
  vtype int not null,
  primary key (id),
  constraint fk1_vehicle_type foreign key (vtype) references vehicle_type (id)
);

create unique index vehicle_unique on vehicle (upper(name));

-- Data types example

create table item (
  id int,
  description varchar(200),
  price decimal(14, 2),
  created_on timestamp,
  active boolean,
  icon bytea, -- blob
  store_code uuid -- object
);

-- Complex Names

create table "car#part$Price" (
  "part#" int,
  "price$dollar" int,
  "%discount" int
);

-- ======================
-- Auto-generated columns 
-- ======================

create table test_sequence1 (
  id1 int not null,
  name varchar(40) not null
);

create table test_sequence2 (
  id1 int not null,
  id2 int not null,
  name varchar(40) not null
);

create table test_identity1 (
  id int not null generated always as identity,
  name varchar(40) not null
);

create table test_identity2 (
  id int not null generated by default as identity,
  id2 serial not null,
  name varchar(40) not null
);

alter sequence test_identity2_id2_seq restart 50;

create sequence gen_seq1 restart 100;
create sequence gen_seq2 restart 200;

create table test_mixed1 (
  id serial not null,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null
);

create table test_seq_ide_def1 (
  id serial not null,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null,
  price integer default 1000 not null,
  branch_id integer default 123 not null
);

alter sequence test_seq_ide_def1_id_seq restart 90;

create table test_default1 (
  name varchar(40) not null,
  price integer default 1000 not null 
);

create table test_default2 (
  name varchar(40) not null,
  price integer default 1000 not null,
  branch_id integer default 123 not null
);

create table event (
  id bigint primary key not null,
  name varchar(20) not null,
  status int not null
);

-- =========================
-- Window functions & frames
-- =========================

create table island (
  id int,
  segment int,
  x_start int,
  x_end int,
  height int
);

insert into island (id, segment, x_start, x_end, height) values (1, 10, 1, 4, 500);
insert into island (id, segment, x_start, x_end, height) values (2, 10, 1, 2, 501);
insert into island (id, segment, x_start, x_end, height) values (3, 10, 2, 3, 502);
insert into island (id, segment, x_start, x_end, height) values (4, 10, 3, 4, 503);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 8, 504);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 6, 505);
insert into island (id, segment, x_start, x_end, height) values (6, 10, 7, 9, 506);
insert into island (id, segment, x_start, x_end, height) values (10, 12, 3, 5, 400);
insert into island (id, segment, x_start, x_end, height) values (11, 12, 1, 3, 401);
insert into island (id, segment, x_start, x_end, height) values (12, 12, 4, 6, 402);
insert into island (id, segment, x_start, x_end, height) values (13, 12, 7, 9, 403);
insert into island (id, segment, x_start, x_end, height) values (14, 12, 7, 9, 404);
insert into island (id, segment, x_start, x_end, height) values (15, 12, 8, 10, 405);


-- =====================
-- Cursor select example
-- =====================

create table cursor_example1 (
  id bigint not null,
  data varchar(1000) not null
);

-- ===============================
-- Column names in different cases
-- ===============================

create table product (
  id bigint not null primary key,
  name varchar(20) not null,
  "Name" varchar(20) not null
);

-- =========
-- All Types
-- =========

create table types_numeric (
  int1 smallint, -- -32768 to +32767
  int2 integer,  -- -2147483648 to +2147483647
  int3 bigint,   -- -9223372036854775808 to +9223372036854775807
  int4 smallserial,
  int5 serial,
  int6 bigserial,
  dec1 decimal(12,2), -- up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
  dec2 numeric(12,2), -- up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
  dec3 decimal(2),
  dec4 decimal(4),
  dec5 decimal(8),
  dec6 decimal(18),
  dec7 decimal(100),
  flo1 real,            -- 6 decimal digits precision
  flo2 double precision -- 15 decimal digits precision
--  mon1 money -- fixed precision: 8 bytes, using by default 2 decimal places.
);

create table types_char (
  cha1 char(10), -- max 1GB
  cha2 varchar(10), -- max 1GB
  cha3 text -- max 1GB
);

create table types_binary (
  bin1 bytea, -- this is like a blob
  bol1 boolean
);

-- Not yet supported: INTERVAL

create table types_date_time (
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

CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');

CREATE TYPE complex AS (
    r       double precision,
    t       double precision
);

create table types_other (
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

-- enum (ideally read-only)

create table employee_state (
  id integer primary key not null,
  description varchar(40) not null
);

create table employee (
  id integer primary key not null,
  name varchar(60) not null,
  state_id integer not null,
  hired_on date not null,
  constraint fk_employee_state foreign key (state_id)
    references employee_state (id)
);

-- =========================
-- Secondary Schema: schema2
-- =========================

create table "schema2".house (
  id int primary key not null,
  name varchar(20)
);

create table house (
  address varchar(50),
  price int
);

create table schema2.account_alert (
  raised_at timestamp not null,
  account_id int not null,
  house_id int not null,
  constraint aa_fk1 foreign key (account_id) references account (id),
  constraint ah_fk2 foreign key (house_id) references schema2.house (id)
);

create view schema2.low_account as select * from account where current_balance < 100;

create table "house_ROOM" (
  id int primary key not null,
  room_name varchar(20),
  house_id int not null,
  constraint room_house_fk1 foreign key (house_id) references schema2.house (id)
);
  
-- create schema "<Stock$";

create table "<Stock$"."&Price%" (
  id int,
  value int
);

create sequence "<Stock$".seq_price;

-- inheritance testing

-- Inheritance modelling limitations in HotRod
-- ===========================================
-- 1. Only the "Permissive Class Table Inheritance" model is implemented.
-- 2. Only single-level inheritance is allowed. Cycles not allowed.
-- 3. Multiple inheritance is not allowed.
-- 4. Symmetric inheritance is not allowed (good).
-- 5. Child tables need to have a single-column PK that points to the parent (PK or UNIQUE constraint).
-- 6. There can be no duplicate property names in the parent and child tables, excluding the PK/FK relationship.
-- 7. Abstract parent is not enforced.
-- 8. Exclusive children is not enforced.

create table a (
  id int primary key not null,
  name varchar(10),
  vin int not null unique
);

create table b (
  id int primary key not null,
  amount int,
--  name int,
  foreign key (id) references a (id)
);

-- @delimiter // solo

create function employee_state_read_only() returns trigger as $BODY$
begin
    raise exception 'Insert, update, or delete operation not allowed on read-only table "employee_state".';
end;
$BODY$ language plpgsql;
//

create trigger employee_state_read_only before insert or update or delete on employee_state
  for each statement execute procedure employee_state_read_only();
//

-- @delimiter ;

create table region (
  region_id int primary key not null,
  name varchar(20) not null unique,
  unified_code int not null,
  population bigint,
  constraint uq101 unique (unified_code),
  constraint fk26 foreign key (unified_code) references region (region_id)
);

create table city (
  city_id int primary key not null,
  city_name varchar(20) not null,
  code_1 int not null,
  code_2 int not null,
  constraint fk20 foreign key (code_1) references region (region_id),
  constraint fk21 foreign key (code_1) references region (unified_code),
  constraint fk22 foreign key (code_2) references region (region_id),
  constraint fk23 foreign key (code_2) references region (unified_code),
  constraint fk24 foreign key (city_name) references region (name)
);

alter table region add
  constraint fk25 foreign key (region_id) references city (city_id);






