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
  id integer not null auto_increment,
  current_balance integer,
  name varchar(100) not null,
  created_on datetime not null,
  primary key (id)
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
  seq_id integer not null auto_increment,
  time varchar(16) not null,
  amount integer not null,
  fed_branch_id int,
  primary key (seq_id),
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

create table agent (
  id integer not null auto_increment,
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
  id integer not null auto_increment,
  name varchar(40) not null,
  mileage integer not null,
  version_number decimal(10) not null,
  primary key (id)
);

-- === Views ===

create view tx_branch
    (account_id, branch_id, branch_name, amount) as
  select t.account_id, b.id, b.name, t.amount 
    from transaction t, federal_branch b 
    where t.fed_branch_id = b.id
    order by t.amount;

-- enum

create table employee_state (
  id integer primary key not null,
  description varchar(40)
);

insert into employee_state (id, description) values (1, 'Enrolled');
insert into employee_state (id, description) values (2, 'Accepted_OK');
insert into employee_state (id, description) values (3, 'Pending Notification');
insert into employee_state (id, description) values (4, 'Rejected, but can Reapply!');

-- ======================
-- Auto-generated columns 
-- ======================

create table test_identity1 (
  id integer not null primary key auto_increment,
  name varchar(40) not null
);

alter table test_identity1 auto_increment = 50;

-- ==================
-- === Data Types ===    
-- ==================

create table types_numeric (
  id int not null,
  
  int10 tinyint, -- synonym: int1
  int11 tinyint unsigned,
  int12 smallint, -- synonym: int2
  int13 smallint unsigned,
  int14 mediumint, -- synonym: int3
  int15 mediumint unsigned,
  int16 int, -- synonyms: integer, int4
  int17 int unsigned,
  int18 bigint, -- synonym: int8
  int19 bigint unsigned,

  float1 float, -- synonym: float4
  float2 float unsigned,
  float3 float(1),
  float04 float(24),
  float5 float(25), -- promotes to double
  float6 float(53),
--  float7 float(54),
  
  float08 real,
  float9 double precision,
  
  double1 double, -- synonym: real, float8, double precision
  double2 double unsigned,

  dec1 decimal(1),
  dec2 decimal(2),
  dec3 decimal(3),
  dec4 decimal(4),
  dec5 decimal(5),
  dec6 decimal(6),
  dec7 decimal(7),
  dec8 decimal(8),
  dec9 decimal(9),
  dec10 decimal(10),
  dec11 decimal(11),
  dec12 decimal(12),
  dec13 decimal(13),
  dec14 decimal(14),
  dec15 decimal(15),
  dec16 decimal(16),
  dec17 decimal(17),
  dec18 decimal(18),
  dec19 decimal(19),
  dec20 decimal(20),

  dec30 decimal, -- synonym: numeric 
  dec31 decimal(10),
  dec32 decimal(10, 0), 
  
  dec40 decimal(10, 2), -- 
--  dec41 decimal(10, -2), -- cannot have negative scale
  dec42 decimal(19, 4), -- BigDecimal
  
  primary key(id)
);

create table types_char (
  id int not null,
  
  letter char(1),
  car_code char(6),
  name varchar(20),
  email tinytext, -- clob of up to 255 chars
  description text, -- clob of up to 65535 chars
  doc mediumtext, -- clob of up to 16M chars
  book longtext, -- clob of up to 4GB chars

  primary key(id)
);

create table types_date_time (
  id int not null,

  today date,
  now datetime,
  right_now timestamp,
  hour_dif time,
  initial_year year,
  
  primary key(id)
);

create table types_binary (
  id int not null,

  ip tinyblob,
  buffer blob,
  image mediumblob,
  program longblob,
  
  primary key(id)
);

create table types_other (
  id int not null,

  color_value enum('yellow', 'red', 'blue', 'green', 'white'),
  included set('one', 'two', 'three', 'four', 'five', 'seven'),
  
  primary key (id)
);


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
