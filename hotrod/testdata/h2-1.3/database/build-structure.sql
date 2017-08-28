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

create sequence seq_house;

create table house (
  id integer primary key not null,
  address varchar(50) not null
);

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
  id integer identity(50) primary key not null,
  name varchar(40) not null
);

create sequence gen_seq1 start with 100;
create sequence gen_seq2 start with 200;

create table test_mixed1 (
  id integer identity primary key not null,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null
);

-- =====
-- Types  
-- =====

create table types_numeric (
  id identity primary key not null,

  i1 int,       -- Integer
  i2 integer,   -- Integer
  i3 mediumint, -- Integer
  i4 int4,      -- Integer
  i5 signed,    -- Integer

  i10 tinyint,  -- Byte
  
  i20 smallint, -- Short
  i21 int2,     -- Short
  i22 year,     -- Short
  
  i30 bigint,   -- Long
  i31 int8,     -- Long
  
  -- i40 identity, -- Long
  
  dec1 decimal(10, 2), -- BigDecimal
  dec2 decimal(19, 4), -- BigDecimal
  
  dec3 numeric(10, 2), -- BigDecimal
  dec4 number(10, 2),  -- BigDecimal
  dec5 dec(10, 2),     -- BigDecimal

  dou1 double,           -- Double
  dou2 double precision, -- Double
  dou3 float,            -- Double
  dou4 float8,           -- Double
  
  rea1 real,   -- Float
  rea2 float4  -- Float
  
);

create table types_char (
  id integer identity primary key not null,

  vc1 varchar(100),                -- String
  vc2 longvarchar(100),            -- String
  vc3 varchar2(100),               -- String
  vc4 nvarchar(100),               -- String
  vc5 nvarchar2(100),              -- String
  vc6 varchar_casesensitive(100),  -- String
  vc7 varchar_ignorecase(100),     -- String
  
  cha1 char(100),      -- String
  cha2 character(100), -- String
  cha3 nchar(100),     -- String
  
  clo1 clob(1000000),       -- java.sql.Clob
  clo2 tinytext(1000000),   -- java.sql.Clob
  clo3 text(1000000),       -- java.sql.Clob
  clo4 mediumtext(1000000), -- java.sql.Clob
  clo5 longtext(1000000),   -- java.sql.Clob
  clo6 ntext(1000000),      -- java.sql.Clob
  clo7 nclob(1000000)       -- java.sql.Clob
  
);

create table types_date_time (
  id integer identity primary key not null,

  tim1 time, -- java.sql.Time
  
  dat1 date, -- java.sql.Date
  
  ts1 timestamp,     -- java.sql.Timestamp
  ts2 datetime,      -- java.sql.Timestamp
  ts3 smalldatetime -- java.sql.Timestamp
  
  -- tz1 timestamp with timezone -- org.h2.api.TimestampWithTimeZone
  
);

create table types_binary (
  id integer identity primary key not null,

  bin1 binary(100),        -- byte[]; max 2GGB.
  bin2 varbinary(100),     -- byte[]; max 2GGB.
  bin3 longvarbinary(100), -- byte[]; max 2GGB.
  bin4 raw(100),           -- byte[]; max 2GGB.
  bin5 bytea(100),         -- byte[]; max 2GGB.
  
  blo1 blob(1000000),       -- java.sql.Blob
  blo2 tinyblob(1000000),   -- java.sql.Blob
  blo3 mediumblob(1000000), -- java.sql.Blob
  blo4 longblob(1000000),   -- java.sql.Blob
  blo5 image(1000000),      -- java.sql.Blob
  blo6 oid(1000000)         -- java.sql.Blob
  
);

create table types_other (
  id integer identity primary key not null,
  
  boo1 boolean, -- java.lang.Boolean
  boo2 bit,     -- java.lang.Boolean
  boo3 bool,    -- java.lang.Boolean
  
  oth1 other, -- java.lang.Object
  
  id1 uuid, -- java.util.UUID
  
  -- arr1 array, -- java.lang.Object[]
  
  geo1 geometry -- java.lang.Object / java.lang.String
  
);

