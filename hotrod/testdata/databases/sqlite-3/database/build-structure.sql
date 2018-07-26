-- pragma foreign_keys = ON;

create table log (
  recorded_at integer,
  notes text
);

create table parameters (
  system integer,
  level2 integer,
  name text,
  value text
);

create index param_i1 on parameters (system, level2);
create index param_i2 on parameters (name);

create table properties (
  application text,
  name text not null,
  prop_value text,
  constraint props_name_uc unique (name)
);

create index properties_i1 on properties (application);

create table config_values (
  node integer not null,
  cell integer not null,
  name text not null,
  verbatim text,
  constraint cfgval_uc1 unique (node, cell),
  constraint cfgval_uc2 unique (name)
);

create table account (
  id integer identity primary key not null,
  current_balance integer,
  name text not null,
  created_on integer not null
);

create table state_branch (
  id integer not null,
  name text not null,
  primary key (id)
);

create table federal_branch (
  id integer not null,
  name text not null,
  primary key (id)
);

create table "transaction" (
  account_id integer not null,
  seq_id integer identity primary key not null,
  time text not null,
  amount integer not null,
  fed_branch_id integer,
  constraint tx_account_id_time unique (account_id, time),
  constraint fk_tx_fed_branch foreign key (fed_branch_id)
    references federal_branch (id)
);

create table client (
  id integer not null,
  national_id integer not null,
  name text not null,
  prop_name text not null,
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

-- create sequence seq_agent;
-- create sequence seq_codes;

create table agent (
  id integer not null,
  name text not null,
  client_id integer,
  primary key (id)
);

create table quadrant (
  region integer not null,
  area integer not null,
  caption text,
  primary key (region, area)
);

create table codes (
  id integer not null,
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
  config_name text,
  config_value text
);

create table vehicle (
  id integer identity primary key not null,
  name text not null,
  mileage integer not null,
  version_number real not null
);

-- create sequence seq_house;

create table house (
  id integer primary key not null,
  address text not null
);

create view tx_branch
    (account_id, branch_id, branch_name, amount) as
  select t.account_id, b.id, b.name, t.amount 
    from "transaction" t, federal_branch b 
    where t.fed_branch_id = b.id
    order by t.amount;

-- =====
-- Types  
-- =====

create table types_numeric (
  id identity primary key not null,
  int1 int,    -- signed integer (1, 2, 3, 4, 6, or 8 bytes depending on the magnitude)
  int2 integer,
  int3 tinyint,
  int4 smallint,
  int5 mediumint,
  int6 bigint,
  int7 unsigned big int,
  int8 int2,
  int9 int8,
  dou1 real,    -- double (8 bytes)
  dou2 double,
  dou3 double precision,
  dou4 float,
  num1 numeric, 
  num2 decimal(10,5)
);

create table types_char (
  id integer identity primary key not null,
  cha1 character(20),
  cha2 nchar(55),
  cha3 native character(70),
  vc1 varchar(255),
  vc2 varying character(255),
  vc3 nvarchar(100),
  vc4 text,
  vc5 clob,
  vc6 string
);

create table types_binary (
  id integer identity primary key not null,
  bin1 blob        -- byte[]; 1,000,000,000 bytes
);

create table types_other (
  id integer identity primary key not null,
  nul1 null,  -- (null?)
  boo1 boolean,
  dat1 date, 
  ts1 datetime
);
