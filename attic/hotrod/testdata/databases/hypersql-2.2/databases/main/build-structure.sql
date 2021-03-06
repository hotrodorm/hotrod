create table log (
  recorded_at date,
  notes varchar(100)
);

create table parameters (
  system integer,
  level2 integer,
  name varchar(50),
  value varchar(100)
);

create index param_i1 on parameters (system, level2);
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

create table account (
  id integer identity primary key not null,
  current_balance integer,
  name varchar(100) not null,
  created_on timestamp not null
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
  seq_id integer identity primary key not null,
  time varchar(16) not null,
  amount integer not null,
  fed_branch_id bigint,
  constraint tx_account_id_time unique (account_id, time),
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
create sequence seq_account;
create sequence seq_transaction;
create sequence seq_codes;

create table agent (
  id integer not null,
  name varchar(40) not null,
  client_id bigint,
  primary key (id)
);

create table quadrant (
  region integer not null,
  area integer not null,
  caption varchar(20),
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
  config_name varchar(150),
  config_value varchar(250)
);

create table vehicle (
  id integer identity primary key not null,
  name varchar(40) not null,
  mileage integer not null,
  version_number decimal(10) not null
);

-- === Views ===

create view tx_branch
    (account_id, branch_id, branch_name, amount) as
  select t.account_id, b.id, b.name, t.amount 
    from transaction t, federal_branch b 
    where t.fed_branch_id = b.id
    order by t.amount;

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
  id integer generated by default as identity (start with 50) primary key not null,
  name varchar(40) not null
);

create sequence gen_seq1 start with 100;
create sequence gen_seq2 start with 200;

create table test_mixed1 (
  id integer generated always as identity (start with 50) primary key not null,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null
);

create table test_seq_ide_def1 (
  id integer generated always as identity not null ,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null,
  price integer default 1000 not null,
  branch_id integer default 123 not null
);

-- ==================
-- === Data Types ===    
-- ==================

create table types_numeric (
  id integer identity primary key not null,

  int1 tinyint, -- Byte
  int2 smallint, -- Short
  int3 integer, -- Integer
  int4 bigint, -- Long
  int5 numeric, -- BigInteger (128 decimal digits)
  int6 decimal, -- BigInteger (128 decimal digits)
  int7 numeric(10), -- BigInteger
  int8 numeric(10,0), -- BigInteger
  int9 decimal(10), -- BigInteger
  int10 decimal(10, 0), -- BigInteger
  
  dec1 numeric(10, 2), -- BigDecimal
--  dec2 numeric(10, -2), -- cannot have negative scale
  dec3 decimal(10, 2), -- BigDecimal
  dec4 decimal(19, 4), -- BigDecimal
  
  dec11 decimal(1),
  dec12 decimal(2),
  dec13 decimal(3),
  dec14 decimal(4),
  dec15 decimal(5),
  dec16 decimal(6),
  dec17 decimal(7),
  dec18 decimal(8),
  dec19 decimal(9),
  dec20 decimal(10),
  dec21 decimal(11),
  dec22 decimal(12),
  dec23 decimal(13),
  dec24 decimal(14),
  dec25 decimal(15),
  dec26 decimal(16),
  dec27 decimal(17),
  dec28 decimal(18),
  dec29 decimal(19),
  dec30 decimal(20),
  
  float1 real, -- Double
  float2 float, -- Double
  float3 double -- Double
  
);

create table types_char (
  id integer identity primary key not null,

  cha1 char, -- synonym: character; defaults to 1 char
--  cha2 char(0), -- minimum length is 1
  cha3 char(1), 
  cha4 char(100),

--  vc1 varchar, -- length must be specified for varchar
--  vc2 varchar(0), -- minimum length is 1
  vc3 varchar(1), -- synonyms: character varying, char varying
  vc4 varchar(100),
  vc5 longvarchar, -- defaults to 16MB; by configuration can also be mapped as clob.
  vc6 longvarchar(1),
  vc7 longvarchar(100),
--  vc8 longvarchar(101K), -- no multipliers (K, M, G) allowed on char/varchar
  
  clo1 clob, -- synonyms: character large object, char large object; defaults to 1GB
--  clo2 clob(0), -- minimum length is 1
  clo3 clob(1),
  clo4 clob(100),
  clo5 clob(102K)
  
);

create table types_date_time (
  id integer identity primary key not null,
  
  dat0 date, -- cannot store time zone
  dat1 time, -- no time zone by default
  dat2 timestamp, -- no time zone by default
  dat3 time with time zone,
  dat4 timestamp with time zone,
  
  dat10 time(0),
  dat11 time(3),
  dat12 time(6),
  dat13 time(9), -- up to nanoseconds precision (9 decimal places)
  
  dat20 timestamp(0),
  dat21 timestamp(3),
  dat22 timestamp(9) -- up to nanoseconds precision (9 decimal places)

);

create table types_binary (
  id integer identity primary key not null,

  bin0 binary, -- defaults to 1 byte
--  bin1 binary(0), -- minimum length is 1
  bin2 binary(1),
  bin3 binary(100),
--  bin4 binary(1M), -- no multipliers (K, M, G) allowed on binary 
  
--  bin10 varbinary, -- length must be specified for varbinary
--  bin11 varbinary(0), -- minimum length is 1
  bin12 varbinary(1), -- synonym: binary varying
  bin13 varbinary(100),
--  bin14 varbinary(1M), -- no multipliers (K, M, G) allowed on varbinary 

  -- # longvarbinary is synonym of varbinary (unless HSQLDB is configured to be synonym of blob).
  -- # Therefore, avoid longvarbinary since it can be confusing.

  bin20 blob, -- synonym: binary large object; defaults to 1GB
--  bin21 blob(0), -- minimum length is 1
  bin22 blob(1),
  bin23 blob(100),
  bin24 blob(1M)

--  bin30 UUID -- a 128-bit binary value, very similar to binary(16) but with different comparator logic.
-- # UUID type not supported on version 2.2.9 of HSQLDB
  
);

create table types_other (
  id integer identity primary key not null,
  
  bool0 boolean, -- Boolean. Has three possible values: TRUE, FALSE, UNKNOWN
                 -- UNKNOWN is converted to a null value
                 -- Any integer is converted to true if different from 0, and false if zero.
  
  bit0 bit, -- defaults to length 1
--  bit1 bit(0), -- minimum length is 1
  bit2 bit(1),
  bit3 bit(100),
--  bit4 bit(1M) -- no multipliers (K, M, G) allowed on binary
  
--  bit10 bit varying -- length must be specified for bit varying
--  bit11 bit varying(0), -- minimum length is 1
  bit12 bit varying(1),
  bit13 bit varying(100),
--  bit14 bit varying(1M), -- no multipliers (K, M, G) allowed on binary
  
  oth0 other, -- stores any serializable java object
  
  itv0 interval year,
  itv1 interval month,
  itv2 interval day,
  itv3 interval hour,
  itv4 interval minute,
  itv5 interval second,
  itv6 interval year to month,
  itv7 interval day to hour,
  itv8 interval day to second,
  itv9 interval minute to second,
  
  arr0 int array

);




    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
