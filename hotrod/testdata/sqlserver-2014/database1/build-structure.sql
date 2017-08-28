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
  id int primary key identity not null,
  name varchar(20) not null,
  current_balance int,
  created_on datetime2 not null
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
  seq_id int not null,
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

create sequence seq_agent;

create table agent (
  id int not null,
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
  id int not null,
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

create sequence gen_seq1 start with 100;

create sequence gen_seq2 start with 200;

create table test_identity1 (
  id int identity not null,
  name varchar(40) not null
);

dbcc checkident('[test_identity1]', reseed, 50);

create table test_mixed1 (
  id int identity not null,
  name varchar(40) not null,
  extra_id1 integer not null,
  extra_id2 integer not null
);

dbcc checkident('[test_mixed1]', reseed, 70);

-- =====
-- Types
-- =====

create table types_numeric (
  id int not null primary key,

  num1 bit,
  num2 tinyint,
  num3 smallint,
  num4 int,
  num5 bigint,

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

  num50 float, -- ::= float(53)
  num51 float(1), -- single precision
  num52 float(24), -- single precision
  num53 float(25), -- double precision
  num54 float(53), -- double precision
  num55 real -- ::= float(24)
    
);

create table types_char (
  id int not null primary key,
  
  cha1 char(10), -- 1 to 8000 chars, non-unicode
    -- synonym: character
  
  cha2 varchar(20), -- 1 to 8000 chars, non-unicode
    -- synonyms: charvarying, charactervarying

  cha3 varchar(max), -- 2^31 - 1 chars
    -- synonyms: charvarying, charactervarying

  cha4 nchar(10), -- 1 to 4000 chars, unicode
    -- synonyms: national char, national character

  cha5 nvarchar(20), -- 1 to 4000 chars, unicode
    -- synonyms: national char varying, national character varying

  cha6 nvarchar(max), -- 2^30 - 1 chars, unicode
    -- synonyms: national char varying, national character varying

  cha10 text, -- [DEPRECATED] 2^31 - 1 chars, non-unicode 

  cha11 ntext -- [DEPRECATED] 2^30 - 1 chars, unicode

);

create table types_date_time (
  id int not null primary key,

  dat1 date, -- just the day, no time info
  
  dat2 datetime, -- accuracy: 3-7ms
  
  dat3 datetime2, -- accuracy: 100 ns
  dat4 datetime2(0), -- accuracy: 100ms - 1s 
  dat5 datetime2(7), -- accuracy: 100ns - 10µs
  
  dat6 datetimeoffset, -- accuracy: 100ms 
  dat7 datetimeoffset(0), -- accuracy: 100ms - 1s 
  dat8 datetimeoffset(7), -- accuracy: 100ns - 10µs
   
  dat9 smalldatetime, -- accuray: 1 minute
  
  tim1 time, -- accuracy: 1ns
  tim2 time(0), -- accuracy: 100ms - 1s
  tim3 time(7) -- accuracy: 100ns - 10µs
  
);

create table types_binary (
  id int not null primary key,
  
  bin1 binary(10), -- 1 to 8000 bytes
  bin2 varbinary(10), -- 1 to 8000 bytes
  bin3 varbinary(max), -- 2^31 - 1 bytes
  bin4 image -- [DEPRECATED] 2^31 - 1 bytes 

);

create table types_other (
  id int not null primary key,

--  cur1 cursor, - ingore for tables/views
  hie1 hierarchyid,  -- Example: /0.3.-7/ ; maybe a String or Object
  row1 rowversion, -- 8 bytes - maybe a long? -- synonym: timestamp -- Cannot be inserted
  uni1 uniqueidentifier, -- 16-byte GUID; considered a String-like type.
--  var1 sql_variant, -- Not supported by the SQL Server JDBC driver.

  xml1 xml, -- Untyped XML; max 2 GB size; A String?
--  xml2 xml (<collection>), -- Typed XML
--  xml3 xml (document <collection>), -- Typed XML
--  xml4 xml (content <collection>), -- Typed XML
  
--  tab1 table, -- Maybe Object?

   geog1 geography,
   geog2 as geog1.STAsText(), -- pseudo column: not supported on insertion
   
   geom1 geometry,
   geom2 as geom1.STAsText() -- pseudo column: not supported on insertion
  
);

-- alter table types_other add constraint default_timestamp default getdate() for row1;



























  


