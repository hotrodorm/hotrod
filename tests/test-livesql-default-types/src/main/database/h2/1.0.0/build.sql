create table numbers (
  id int primary key not null,
  int1 int,       -- Integer
  int2 integer,   -- Integer
  int3 mediumint, -- Integer
  int4 int4,      -- Integer
  int5 signed,    -- Integer
  int10 tinyint,  -- Byte
  int20 smallint, -- Short
  int21 int2,     -- Short
  -- int22 year,     -- Short
  int30 bigint,   -- Long
  int31 int8,     -- Long
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

create table chars (
  id int primary key not null,

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

create table dates (
  id int primary key not null,

  tim1 time, -- java.sql.Time
  
  dat1 date, -- java.sql.Date
  
  ts1 timestamp,     -- java.sql.Timestamp
  ts2 datetime,      -- java.sql.Timestamp
  ts3 smalldatetime -- java.sql.Timestamp
  
  -- tz1 timestamp with timezone -- org.h2.api.TimestampWithTimeZone
);

create table binaries (
  id int primary key not null,

  bin1 binary(100),        -- byte[]; max 2GGB.
  bin2 varbinary(100),     -- byte[]; max 2GGB.
  bin3 longvarbinary(100), -- byte[]; max 2GGB.
  bin4 raw(100),           -- byte[]; max 2GGB.
  bin5 bytea(100),         -- byte[]; max 2GGB.
  
  blo1 blob(1000000),       -- java.sql.Blob
  blo2 tinyblob(1000000),   -- java.sql.Blob
  blo3 mediumblob(1000000), -- java.sql.Blob
  blo4 longblob(1000000),   -- java.sql.Blob
  blo5 image(1000000)      -- java.sql.Blob
  -- blo6 oid(1000000)         -- java.sql.Blob
);

create table other (
  id int primary key not null,
  
  boo1 boolean, -- java.lang.Boolean
  boo2 bit,     -- java.lang.Boolean
  boo3 bool,    -- java.lang.Boolean
  
  oth1 other, -- java.lang.Object
  
  idn1 uuid, -- java.util.UUID
  
  -- arr1 array, -- java.lang.Object[]
  
  geo1 geometry -- java.lang.Object / java.lang.String
);  

-- ==========================================================================================================

create table product (
  id int,
  type varchar(6),
  shipping int
);

create table branch (
  id int generated by default as identity primary key not null,
  region varchar(10),
  is_vip int,
  created_at timestamp
);

insert into branch (id, region, is_vip, created_at) values
  (101, 'N', true, '2024-01-01 12:34:56'),
  (102, 'S', true, '2024-01-02 12:34:56'),
  (103, 'W', false, '2024-01-03 12:34:56'),
  (104, 'E', false, '2024-01-04 12:34:56'),
  (105, 'NE', false, '2024-01-05 12:34:56'),
  (106, 'NW', 1, '2024-01-06 12:34:56'),
  (107, 'SE', false, '2024-01-07 12:34:56');

create table account (
  id int primary key not null,
  parent_id int references account (id),
  branch_id int references branch (id)
);

insert into account (id, parent_id, branch_id) values
  (1, null, 101),
  (2, 1, 102),
  (3, 1, 103),
  (4, 3, 104),
  (5, 4, 105);

create sequence seq1;

create table invoice (
  id int,
  account_id int,
  amount int,
  branch_id int,
  order_date date,
  type varchar(10),
  unpaid_balance int,
  status varchar(10)
);

insert into invoice (id, account_id, amount, branch_id, order_date, type, unpaid_balance, status) values
  (10, 123, 100, 2, '2024-03-15', 'VIP', 0, 'CONF'),
  (11, 125, 500, 2, '2024-03-16', 'NORTH', 50, 'PLACED');

create table invoice_line (
  invoice_id int,
  product_id int,
  line_total int
);

create table payment (
  payment_date date,
  invoice_id int,
  amount int
);

  
  
  -- update branch set region = 'x' where id >= 4 and not is_vip


