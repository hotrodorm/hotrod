create table log (
  recorded_at date,
  notes varchar(100)
);

create table parameters (
  system number(9),
  level2 number(9),
  name varchar2(50),
  value varchar2(100)
);

create index param_i1 on parameters (system, level2);
create index param_i2 on parameters (name);

create table properties (
  application varchar2(20),
  name varchar2(50) not null,
  prop_value varchar2(80),
  constraint props_name_uc unique (name)
);

create index properties_i1 on properties (application);

create table config_values (
  node number(9) not null,
  cell number(9) not null,
  name varchar2(20) not null,
  verbatim varchar2(50) default '{no-verbatim}',
  constraint cfgval_uc1 unique (node, cell),
  constraint cfgval_uc2 unique (name)
);

create table account (
  id number(9) not null,
  name varchar2(20) not null,
  type varchar2(10) not null,
  current_balance number(9),
  created_on date,
  row_version number(9) not null,
  primary key (id)
);

create view hefty_account as
  select * from account where current_balance >= 10000;

create table state_branch (
  id number(9) not null,
  name varchar2(30) not null,
  primary key (id)
);

create table federal_branch (
  id number(9) not null,
  name varchar2(30) not null,
  primary key (id)
);

create table transaction (
  account_id number(9) not null,
  seq_id number(9) not null,
  time varchar2(16) not null,
  amount number(9) default -1 not null,
  fed_branch_id number(18),
  primary key (seq_id),
  constraint tx_account_id_time unique (account_id, time),
  constraint fk_tx_fed_branch foreign key (fed_branch_id)
    references federal_branch (id)
);

create table client (
  id number(9) not null,
  national_id number(9) not null,
  name varchar2(40) not null,
  prop_name varchar2(20) not null,
  referrer_id number(9),
  friend_id number(9),
  group_account_id number(9),
  branch_id number(9),
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
create sequence seq_account;
create sequence seq_transaction;
create sequence seq_codes;

create table agent (
  id number(9) not null,
  name varchar2(40) not null,
  client_id number(18),
  primary key (id)
);

create table deputy (
  id number(12) primary key not null,
  title varchar2(50) not null,
  agent_id number(12) not null,
  constraint fk_deputy_agent foreign key (agent_id) references agent (id)
);

create table quadrant (
  region number(9) not null,
  area number(9) not null,
  caption varchar2(20),
  active_state number(4) not null,
  primary key (region, area)
);

create table codes (
  id number(9) not null,
  account number(9) not null,
  version_name number(9) not null,
  account_version number(9) not null,
  name number(9) not null,
  constraint fk_codes_q1 foreign key (account, version_name) 
    references quadrant (region, area),
  constraint fk_codes_q2 foreign key (account_version, name) 
    references quadrant (region, area),
  constraint cd_q1 unique (account, version_name),
  constraint cd_q2 unique (account_version, name)
);

create table application_config (
  config_id number(9) not null,
  config_name varchar(150),
  config_value varchar(250)
);

create view tx_branch (account_id, branch_id, branch_name, amount) as
select t.account_id, b.id, b.name, t.amount from transaction t, federal_branch b 
  where t.fed_branch_id = b.id
  order by t.amount;


create sequence seq_test;

create table test_sequence (
  id number(9) not null,
  name varchar(20)
);

-- =========
-- All Types
-- =========

create table types_numeric (
  id number(9) not null primary key,
  
-- number, numeric, decimal, dec
  num1 number(2), -- max 38 digits
  num2 number(4), -- max 38 digits
  num3 number(9), -- max 38 digits
  num4 number(18), -- max 38 digits
  num5 number(38), -- max 38 digits
  num6 number(10,2), -- max 38 digits
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

create table types_char (
  id number(9) not null primary key,
  
  cha1 char(10), -- max 2000
  cha2 varchar(20), -- max 4000
  cha3 varchar2(20), -- max 4000
  cha4 nchar(30), -- max 2000 bytes (2000 or less characters)
  cha5 nvarchar2(40), -- max 4000 (4000 or less characters)
  cha6 clob, -- max 128 TB
  cha7 nclob -- max 128 TB
  -- cha10 long -- max 2 GB -- unsupported by MyBatis?
);

create table types_date_time (
  id number(9) not null primary key,
  
  -- all types include dates in the range January 1, 4712 BCE through December 31, 9999 CE
  dat1 date, -- NO fractional seconds
  dat2 timestamp, -- WITH fractional seconds
  dat3 timestamp with time zone, -- WITH time zone, WITH fractional seconds
  dat4 timestamp with local time zone -- WITH relative time zone, WITH fractional seconds
);

-- Not yet supported: bfile

create table types_binary (
  id number(9) not null primary key,
  
  bin1 raw(500), -- Deprecated. Still usable. Size must be specified.
  bin2 long raw, -- Deprecated. Still usable, max 2GB; only 1 long raw column per table.
  bin3 blob -- 128 TB
  --bin4 bfile -- Deprecated. Read-only binary file. Size is limited by the OS/file system.  
);

create type namearray as varray(3) of varchar2(10);

create type person_struct as object (id number(6), date_of_birth date);

create table types_other (
  id number(9) not null primary key,
  
  row1 UROWID,
  itv2 interval year to month,
  itv4 interval day to second,
--  oth1 XMLType, -- not supported by this version of MyBatis?
  oth2 UriType,
  names namearray,
  stu1 person_struct,
  ref1 ref person_struct
);

create view types_extra (col1, col2) as 
  select num1, rowid from types_numeric;

-- database objects with special character in their names

-- The Oracle Database doesn't allow the ASCII characters semi-colon(;), apostrophe ('), or double quotes(")
-- The Oracle JDBC driver crashes with a name that has a slash (/)
-- MyBatis crashes when a column name has an equal sign (=) in it

create table " !#$%)(*+,-." (
  id number(6) not null primary key,
  ":<>?&" varchar2(12) not null,
  "@[\]^_" varchar2(12) not null,
  "`{|}~" varchar2(12) not null
);

-- enum

create table employee_state (
  id number(6) primary key not null,
  since date,
  description varchar(40) not null,
  active number(4) not null
);

create table employee_interim (
  start_date date primary key not null,
  caption varchar2(100) not null
);

create table employee (
  id integer primary key not null,
  name varchar(60) not null,
  state_id number(6) not null,
  initial_state_id number(10) not null,
  hired_on date not null,
  classification date not null,
  constraint fk_employee_state foreign key (state_id) references employee_state (id),
  constraint fk_employee_state_initial foreign key (initial_state_id) references employee_state (id),
  constraint fk_employee_classification foreign key (classification) references employee_interim (start_date)
);

-- unsupported multi-reference FKs

create table house_type (
  local_code number(6) primary key not null,
  name varchar(40) not null,
  federal_code number(6) not null,
  long_description varchar(1000) not null,
  state_code number(6) not null,
  constraint ht_ordinal unique (federal_code),
  constraint ht_numeral unique (state_code)
);

create table house (
  house_id number(12) primary key not null,
  address varchar(200) not null,
  type number(6) not null,
  constraint fk_house_t1 foreign key (type) references house_type (local_code),
  constraint fk_house_t2 foreign key (type) references house_type (federal_code),
  constraint fk_house_t3 foreign key (type) references house_type (state_code)
);


