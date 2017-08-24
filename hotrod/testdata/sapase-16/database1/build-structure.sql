create table log (
  recorded_at datetime,
  notes varchar(100)
);

create table parameters (
  system int,
  level2 int,
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
  node int not null,
  cell int not null,
  name varchar(20) not null,
  verbatim varchar(50),
  constraint cfgval_uc1 unique (node, cell),
  constraint cfgval_uc2 unique (name)
);

create table account (
  id int identity not null,
  name varchar(20) not null,
  current_balance int,
  created_on datetime not null,
  primary key (id)
);

create table state_branch (
  id int not null,
  name varchar(30) not null,
  primary key (id)
);

create table federal_branch (
  id int not null,
  name varchar(30) not null,
  primary key (id)
);

create table transaction2 (
  account_id int not null,
  seq_id int identity not null,
  time varchar(16) not null,
  amount int not null,
  fed_branch_id int,
  primary key (seq_id),
  constraint tx_account_id_time unique (account_id, time),
  constraint fk_tx_fed_branch foreign key (fed_branch_id)
    references federal_branch (id)
);

create table client (
  id bigint not null,
  national_id int not null,
  name varchar(40) not null,
  prop_name varchar(50) not null,
  referrer_id bigint,
  friend_id int,
  group_account_id int,
  branch_id int,
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

create table agent (
  id int identity not null,
  name varchar(40) not null,
  client_id bigint,
  primary key (id)
);

create table quadrant (
  region int not null,
  area int not null,
  caption varchar(20),
  primary key (region, area)
);

create table codes (
  id int identity not null,
  account int not null,
  version_name int not null,
  account_version int not null,
  name int not null,
  constraint fk_codes_q1 foreign key (account, version_name) 
    references quadrant (region, area),
  constraint fk_codes_q2 foreign key (account_version, name) 
    references quadrant (region, area),
  constraint cd_q1 unique (account, version_name),
  constraint cd_q2 unique (account_version, name)
);

create table application_config (
  config_id int not null,
  config_name varchar(150),
  config_value varchar(250)
);

create view tx_branch (account_id, branch_id, branch_name, amount) as
select t.account_id, b.id, b.name, t.amount from transaction2 t, federal_branch b 
  where t.fed_branch_id = b.id;

create table test_identity (
  id int identity not null primary key,
  name varchar(20)
);

-- =====
-- Types
-- =====

create table types_numeric (
  id int not null primary key,

  num1 bit,
  num2 tinyint,
  num3 unsigned tinyint,
  num4 smallint,
  num5 unsigned smallint,
  num6 int, -- synonym: integer
  num7 unsigned int,
  num8 bigint,
  num9 unsigned bigint,

  num10 decimal, -- synonym: numeric
  num11 decimal(1),
  num12 decimal(2),
  num13 decimal(3),
  num14 decimal(4),
  num15 decimal(5),
  num16 decimal(6),
  num17 decimal(7),
  num18 decimal(8),
  num19 decimal(9),
  num20 decimal(10),
  num21 decimal(17),
  num22 decimal(18),
  num23 decimal(19),

  num25 decimal(10,0),
  num26 decimal(10,2),
  num27 decimal(10,8),

  num40 money,
  num41 smallmoney,

  num50 float, -- ::= double precision
  num51 float(1), -- double precision
  num52 float(24), -- double precision
  num53 float(25), -- double precision
  num54 float(48), -- double precision
  num55 real, -- ::= double precision
  num56 double precision -- ::= double precision
    
);

create table types_char (
  id int not null primary key,
  
  cha1 char(10), --   1 <= n <= pagesize (2K, 4K, 8K or 16K) chars, non-unicode
    -- synonym: character
  
  cha2 varchar(20), -- 1 to 8000 chars, non-unicode
    -- synonyms: charvarying, charactervarying

  cha3 unichar(10), --  1 <= n <= pagesize/@@unicharsize chars, unicode

  cha4 nchar(10), --  1 <= n <= pagesize/@@ncharsize chars, unicode
    -- synonyms: national char, national character

  cha5 nvarchar(20), -- 1 to 4000 chars, unicode
    -- synonyms: national char varying, national character varying

  cha6 univarchar(20), -- 2^30 - 1 chars, unicode

  cha10 text, -- 2^31 - 1 chars, non-unicode 
  cha12 unitext, -- 2^30 - 1 chars, unicode
  
  cha14 longsysname, -- equivalent to: varchar(255) not null
  cha15 sysname -- equivalent to: varchar(30) not null

);

create table types_date_time (
  id int not null primary key,

  dat1 date, -- just the day, no time info
  
  dat2 datetime, -- accuracy: 1 microsecond
   
  dat9 smalldatetime, -- accuray: 1 minute
  
  dat10 bigtime, -- accuracy: 1 microsecond
  dat11 bigdatetime, -- accuracy: 1 microsecond
  
  tim1 time, -- accuracy: 1ns
  -- tim2 time(1), -- accuracy: 100ms - 1s
  -- tim3 time(7) -- accuracy: 100ns - 10µs
  
);

create table types_binary (
  id int not null primary key,
  
  bin1 binary(10), -- 1 to 8000 bytes
  bin2 varbinary(10), -- 1 to 8000 bytes
  bin4 image -- [DEPRECATED] 2^31 - 1 bytes 
             -- even if deprecated, there's no other BLOB-like type.
  -- bin5 uniqueidentifier, -- 16-byte GUID

);

create table types_other (
  id int not null primary key
);



























  


