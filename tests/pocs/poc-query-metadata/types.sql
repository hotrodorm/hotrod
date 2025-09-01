-- ========================================= --
-- Oracle --
-- ========================================= --

create type namearray as varray(3) of varchar2(10);

create type person_struct as object (id number(6), date_of_birth date);

create table all_types_table (
  -- NUMERIC ----------------------------------------------------------------
  -- number, numeric, decimal, dec
  num1 number(2), -- max 38 digits
  num2 number(4), -- max 38 digits
  num3 number(9), -- max 38 digits
  num4 number(18), -- max 38 digits
  num5 number(38), -- max 38 digits
  num6 number(10,2), -- max 38 digits
  num7 number,
  --
  num8 binary_float, -- 32-bit single-precision floating point
  num9 binary_double, -- 64-bit single-precision floating point
  num10 float,
  num11 real,
  num12 double precision,
  --
  num20 smallint,
  num21 integer,
  num22 int,
  -- CHAR -------------------------------------------------------------------
  cha1 char(10), -- max 2000
  cha2 varchar(20), -- max 4000
  cha3 varchar2(20), -- max 4000
  cha4 nchar(30), -- max 2000 bytes (2000 or less characters)
  cha5 nvarchar2(40), -- max 4000 (4000 or less characters)
  cha6 clob, -- max 128 TB
  cha7 nclob, -- max 128 TB
  -- cha10 long -- max 2 GB -- unsupported by MyBatis?
  -- DATETIME -----------------------------------------------------------------
  -- all types include dates in the range January 1, 4712 BCE through December 31, 9999 CE
  dat1 date, -- NO fractional seconds
  dat2 timestamp, -- WITH fractional seconds
  dat3 timestamp with time zone, -- WITH time zone, WITH fractional seconds
  dat4 timestamp with local time zone, -- WITH relative time zone, WITH fractional seconds
  itv2 interval year to month,
  itv4 interval day to second,
  -- BINARY -----------------------------------------------------------------
  bin1 raw(500), -- Deprecated. Still usable. Size must be specified.
  bin2 long raw, -- Deprecated. Still usable, max 2GB; only 1 long raw column per table.
  bin3 blob, -- 128 TB
  --bin4 bfile -- Deprecated. Read-only binary file. Size is limited by the OS/file system.  
  -- OTHER -----------------------------------------------------------------
--  oth1 XMLType, -- not supported by this version of MyBatis?
  oth2 UriType,
  names namearray,
  stu1 person_struct,
  ref1 ref person_struct
);

create view all_types_view as 
select t.*, 
  rowid as rowid_, 
  rownum as rownum_,
  uid as uid_,
  user as user_
from all_types_table t;

Column   Type                            Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ
-------  ------------------------------  ----  ----------  -----  ------  --------  ------  -------------  -------------------------  -----  -----  -----  -----  -----  ------  ----
NUM1     NUMBER                             3           2      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM2     NUMBER                             5           4      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM3     NUMBER                            10           9      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM4     NUMBER                            19          18      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM5     NUMBER                            39          38      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM6     NUMBER                            12          10      2  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM7     NUMBER                            39           0   -127  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM8     BINARY_FLOAT                       4           0      0  true    Yes          100    ?            java.lang.Float            false  false  false  false  false  true    true
NUM9     BINARY_DOUBLE                      8           0      0  true    Yes          101    ?            java.lang.Double           false  false  false  false  false  true    true
NUM10    NUMBER                            39         126   -127  true    Yes            2  NUMERIC        java.lang.Double           false  false  true   false  false  true    true
NUM11    NUMBER                            20          63   -127  true    Yes            2  NUMERIC        java.lang.Double           false  false  true   false  false  true    true
NUM12    NUMBER                            39         126   -127  true    Yes            2  NUMERIC        java.lang.Double           false  false  true   false  false  true    true
NUM20    NUMBER                            39          38      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM21    NUMBER                            39          38      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
NUM22    NUMBER                            39          38      0  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
CHA1     CHAR                              10          10      0  true    Yes            1  CHAR           java.lang.String           false  true   false  false  false  true    true
CHA2     VARCHAR2                          20          20      0  true    Yes           12  VARCHAR        java.lang.String           false  true   false  false  false  true    true
CHA3     VARCHAR2                          20          20      0  true    Yes           12  VARCHAR        java.lang.String           false  true   false  false  false  true    true
CHA4     NCHAR                             30          30      0  true    Yes          -15  NCHAR          java.lang.String           false  true   false  false  false  true    true
CHA5     NVARCHAR2                         40          40      0  true    Yes           -9  NVARCHAR       java.lang.String           false  true   false  false  false  true    true
CHA6     CLOB                            4000          -1      0  true    Yes         2005  CLOB           oracle.jdbc.OracleClob     false  true   false  false  false  false   true
CHA7     NCLOB                           4000          -1      0  true    Yes         2011  NCLOB          oracle.jdbc.OracleNClob    false  true   false  false  false  false   true
DAT1     DATE                               7           0      0  true    Yes           93  TIMESTAMP      java.sql.Timestamp         false  false  false  false  false  true    true
DAT2     TIMESTAMP                         11           0      6  true    Yes           93  TIMESTAMP      oracle.sql.TIMESTAMP       false  false  false  false  false  true    true
DAT3     TIMESTAMP WITH TIME ZONE          13           0      6  true    Yes         -101    ?            oracle.sql.TIMESTAMPTZ     false  false  false  false  false  true    true
DAT4     TIMESTAMP WITH LOCAL TIME ZONE    11           0      6  true    Yes         -102    ?            oracle.sql.TIMESTAMPLTZ    false  false  false  false  false  true    true
ITV2     INTERVALYM                         5           2      0  true    Yes         -103    ?            oracle.sql.INTERVALYM      false  false  false  false  false  true    true
ITV4     INTERVALDS                        11           2      6  true    Yes         -104    ?            oracle.sql.INTERVALDS      false  false  false  false  false  true    true
BIN1     RAW                              500           0      0  true    Yes           -3  VARBINARY      [B                         false  false  false  false  false  true    true
BIN2     LONG RAW                           0  2147483647      0  true    Yes           -4  LONGVARBINARY  [B                         false  false  false  false  false  false   true
BIN3     BLOB                            4000          -1      0  true    Yes         2004  BLOB           oracle.jdbc.OracleBlob     false  false  false  false  false  false   true
OTH2     SYS.URITYPE                     2000           0      0  true    Yes         2002  STRUCT         oracle.jdbc.OracleStruct   false  false  false  false  false  false   true
NAMES    USER1.NAMEARRAY                 2000           0      0  true    Yes         2003  ARRAY          oracle.jdbc.OracleArray    false  false  false  false  false  false   true
STU1     USER1.PERSON_STRUCT             2000           0      0  true    Yes         2002  STRUCT         oracle.jdbc.OracleStruct   false  false  false  false  false  false   true
REF1     USER1.PERSON_STRUCT             2000           0      0  true    Yes         2006  REF            oracle.jdbc.OracleRef      false  false  false  false  false  false   true
ROWID_   ROWID                              1           0      0  true    No            -8  ROWID          oracle.sql.ROWID           false  false  false  false  false  true    true
ROWNUM_  NUMBER                            39           0   -127  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
UID_     NUMBER                            39           0   -127  true    Yes            2  NUMERIC        java.math.BigDecimal       false  false  true   false  false  true    true
USER_    VARCHAR2                          30          30      0  true    Yes           12  VARCHAR        java.lang.String           false  true   false  false  false  true    true


-- ========================================= --
-- DB2 --
-- ========================================= --

create table all_types_table (
  cha1 character(10),     -- 1 to 255 chars. The default length is 1.
  cha2 char(20),          -- 1 to 255 chars.
  cha3 varchar(30),       -- 1 to "page size of the table space" (32672?).
  cha3b character varying(31),
  cha4 clob (40),         -- 1 to 2147483647 chars. The default length is 1M.
  cha5 graphic(50),       -- 1 to 127 "double-byte" chars. The default length is 1.
  cha6 nchar(50),         -- synonym of GRAPHIC
  cha7 vargraphic(60),    -- 1 to "page size  of the table space" (16336?)
  cha8 nvarchar(60),      -- synonym of VARGRAPHIC
  cha9 dbclob(70),        -- 1 to 1073741823(4) double byte chars. The default length is 1M.
  cha10 nclob(70),        -- synonym of DBCLOB
  cha11 long varchar,     -- deprecated: 1 to (?). Cannot specify size.
                          -- max size = (max row size) - (size of all other cols)
  cha12 long vargraphic,  -- deprecated: 1 to (?). Cannot specify size.
  cha13 long varchar for bit data,
  cha14 varchar(55) for bit data,
  cha15 char(56) for bit data,
  -- NUMERIC -----------------------------------------------
  num1 smallint,      -- -32768 to +32767.
  num2 int,           -- -2147483648 to +2147483647.
  num3 integer,       -- -2147483648 to +2147483647.
  num4 bigint,        -- -9223372036854775808 to +9223372036854775807.
  num5 dec(8),        -- Precision 1 to 31; scale 0 to 31.
  num6 numeric(8),
  num7 num(8),
  num8 numeric(10,2),
  num8a decimal(2),
  num8b decimal(4),
  num8c decimal(8),
  num8d decimal(18),
  num8e decimal(31),
  num9 decimal(10,2),  
  num10 decfloat,      -- IEEE 754r. Max precision is 34.
  num11 real,         -- Single-precision floating-point number (4-bytes)
  num12 float,        -- Double-precision floating-point number (8-bytes)
  num13 double,        -- Double-precision floating-point number (8-bytes)
  -- DATETIME -----------------------------------------------
  dat1 date, -- only date, no time: 0001-01-01 to 9999-12-31.
  dat2 time, -- only time (with seconds precision), no date
  dat3 timestamp, -- date, time, with nanosecond precision.
  -- BINARY -----------------------------------------------
  bin1 blob(100),        -- 1 to 2147483647 bytes. The default length is 1M.
  -- OTHER -----------------------------------------------
  oth1 xml,
  oth2 int array[100]
);

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                        Size  Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ 
------  -------------------------  -----  ---------  -----  ------  --------  ------  -------------  -------------------------  -----  -----  -----  -----  -----  ------  -----
CHA1    CHAR                          10         10      0  false   Yes            1  CHAR           java.lang.String           false  true   false  false  true   true    false
CHA2    CHAR                          20         20      0  false   Yes            1  CHAR           java.lang.String           false  true   false  false  true   true    false
CHA3    VARCHAR                       30         30      0  false   Yes           12  VARCHAR        java.lang.String           false  true   false  false  true   true    false
CHA3B   VARCHAR                       31         31      0  false   Yes           12  VARCHAR        java.lang.String           false  true   false  false  true   true    false
CHA4    CLOB                          40         40      0  false   Yes         2005  CLOB           java.sql.Clob              false  true   false  false  true   true    false
CHA5    GRAPHIC                       50         50      0  false   Yes          -15  NCHAR          java.lang.String           false  true   false  false  true   true    false
CHA6    GRAPHIC                       50         50      0  false   Yes          -15  NCHAR          java.lang.String           false  true   false  false  true   true    false
CHA7    VARGRAPHIC                    60         60      0  false   Yes           -9  NVARCHAR       java.lang.String           false  true   false  false  true   true    false
CHA8    VARGRAPHIC                    60         60      0  false   Yes           -9  NVARCHAR       java.lang.String           false  true   false  false  true   true    false
CHA9    DBCLOB                        70         70      0  false   Yes         2011  NCLOB          java.sql.Clob              false  true   false  false  true   true    false
CHA10   DBCLOB                        70         70      0  false   Yes         2011  NCLOB          java.sql.Clob              false  true   false  false  true   true    false
CHA11   LONG VARCHAR               32700      32700      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  true   false  false  true   true    false
CHA12   LONG VARGRAPHIC            16350      16350      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  true   false  false  true   true    false
CHA13   LONG VARCHAR FOR BIT DATA  65400      32700      0  false   Yes           -4  LONGVARBINARY  byte[]                     false  false  false  false  true   true    false
CHA14   VARCHAR FOR BIT DATA         110         55      0  false   Yes           -3  VARBINARY      byte[]                     false  false  false  false  true   true    false
CHA15   CHAR FOR BIT DATA            112         56      0  false   Yes           -2  BINARY         byte[]                     false  false  false  false  true   true    false
NUM1    SMALLINT                       6          5      0  true    Yes            5  SMALLINT       java.lang.Integer          false  false  false  false  true   true    false
NUM2    INTEGER                       11         10      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  false  true   true    false
NUM3    INTEGER                       11         10      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  false  true   true    false
NUM4    BIGINT                        20         19      0  true    Yes           -5  BIGINT         java.lang.Long             false  false  false  false  true   true    false
NUM5    DECIMAL                       10          8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM6    DECIMAL                       10          8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM7    DECIMAL                       10          8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8    DECIMAL                       12         10      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8A   DECIMAL                        4          2      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8B   DECIMAL                        6          4      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8C   DECIMAL                       10          8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8D   DECIMAL                       20         18      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8E   DECIMAL                       33         31      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM9    DECIMAL                       12         10      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM10   DECFLOAT                      42         34      0  true    Yes         1111  OTHER          java.math.BigDecimal       false  false  false  false  true   true    false
NUM11   REAL                          13          7      0  true    Yes            7  REAL           java.lang.Float            false  false  false  false  true   true    false
NUM12   DOUBLE                        22         15      0  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  false  true   true    false
NUM13   DOUBLE                        22         15      0  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  false  true   true    false
DAT1    DATE                          10         10      0  false   Yes           91  DATE           java.sql.Date              false  false  false  false  true   true    false
DAT2    TIME                           8          8      0  false   Yes           92  TIME           java.sql.Time              false  false  false  false  true   true    false
DAT3    TIMESTAMP                     26         26      6  false   Yes           93  TIMESTAMP      java.sql.Timestamp         false  false  false  false  true   true    false
BIN1    BLOB                         200        100      0  false   Yes         2004  BLOB           java.sql.Blob              false  false  false  false  true   true    false
OTH1    XML                            0          0      0  false   Yes         2009  SQLXML         com.ibm.db2.jcc.DB2Xml     false  false  false  false  true   true    false


-- ========================================= --
-- PostgreSQL --
-- ========================================= --

CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');

CREATE TYPE complex AS (
    r       double precision,
    t       double precision
);

create table all_types_table (
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
  flo2 double precision, -- 15 decimal digits precision
  --  mon1 money -- fixed precision: 8 bytes, using by default 2 decimal places.
  -- CHAR -------------------------------------------------------
  cha1 char(10), -- max 1GB
  cha2 varchar(10), -- max 1GB
  cha3 text, -- max 1GB
  -- BINARY -------------------------------------------------------
  bin1 bytea, -- this is like a blob
  bol1 boolean,
  -- DATETIME -------------------------------------------------------
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
  -- OTHER -------------------------------------------------------
  current_mood mood,
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
  bit1 bit(10),
  bit2 bit varying(10),
  uui1 uuid, -- universally unique identifier
  xml1 xml, -- (optional support)
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

create view all_types_view as 
  select t.* from all_types_table t;
  
Column        Type               Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type  Class suggested by driver           ainc   case   curr   dwrit  ronly  search  writ
------------  -----------  ----------  ----------  -----  ------  --------  ------  ---------  ----------------------------------  -----  -----  -----  -----  -----  ------  ----
int1          int2                  6           5      0  true    Yes            5  SMALLINT   java.lang.Integer                   false  false  false  false  false  true    true
int2          int4                 11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  false  false  false  false  true    true
int3          int8                 20          19      0  true    Yes           -5  BIGINT     java.lang.Long                      false  false  false  false  false  true    true
int4          int2                  6           5      0  true    Yes            5  SMALLINT   java.lang.Integer                   false  false  false  false  false  true    true
int5          int4                 11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  false  false  false  false  true    true
int6          int8                 20          19      0  true    Yes           -5  BIGINT     java.lang.Long                      false  false  false  false  false  true    true
dec1          numeric              14          12      2  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec2          numeric              14          12      2  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec3          numeric               3           2      0  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec4          numeric               5           4      0  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec5          numeric               9           8      0  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec6          numeric              19          18      0  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
dec7          numeric             101         100      0  true    Yes            2  NUMERIC    java.math.BigDecimal                false  false  false  false  false  true    true
flo1          float4               15           8      8  true    Yes            7  REAL       java.lang.Float                     false  false  false  false  false  true    true
flo2          float8               25          17     17  true    Yes            8  DOUBLE     java.lang.Double                    false  false  false  false  false  true    true
cha1          bpchar               10          10      0  false   Yes            1  CHAR       java.lang.String                    false  true   false  false  false  true    true
cha2          varchar              10          10      0  false   Yes           12  VARCHAR    java.lang.String                    false  true   false  false  false  true    true
cha3          text         2147483647  2147483647      0  false   Yes           12  VARCHAR    java.lang.String                    false  true   false  false  false  true    true
bin1          bytea        2147483647  2147483647      0  false   Yes           -2  BINARY     [B                                  false  true   false  false  false  true    true
bol1          bool                  1           1      0  false   Yes           -7  BIT        java.lang.Boolean                   false  false  false  false  false  true    true
dat1          date                 13          13      0  false   Yes           91  DATE       java.sql.Date                       false  false  false  false  false  true    true
ts1           timestamp            29          29      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp                  false  false  false  false  false  true    true
ts2           timestamp            29          29      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp                  false  false  false  false  false  true    true
ts3           timestamptz          35          35      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp                  false  false  false  false  false  true    true
ts4           timestamptz          35          35      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp                  false  false  false  false  false  true    true
ts5           timestamp            29          29      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp                  false  false  false  false  false  true    true
tim1          time                 15          15      6  false   Yes           92  TIME       java.sql.Time                       false  false  false  false  false  true    true
tim2          time                 15          15      6  false   Yes           92  TIME       java.sql.Time                       false  false  false  false  false  true    true
tim3          timetz               21          21      6  false   Yes           92  TIME       java.sql.Time                       false  false  false  false  false  true    true
tim4          timetz               21          21      6  false   Yes           92  TIME       java.sql.Time                       false  false  false  false  false  true    true
tim5          time                 15          15      6  false   Yes           92  TIME       java.sql.Time                       false  false  false  false  false  true    true
ivt1          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt2          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt3          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt4          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt5          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt6          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt7          interval             49          49      5  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt8          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt9          interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt10         interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt11         interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt12         interval             49          49      5  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt13         interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt14         interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt15         interval             49          49      5  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt16         interval             49          49  65535  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
ivt17         interval             49          49      5  false   Yes         1111  OTHER      org.postgresql.util.PGInterval      false  false  false  false  false  true    true
current_mood  mood         2147483647  2147483647      0  false   Yes           12  VARCHAR    java.lang.Object                    false  true   false  false  false  true    true
geo1          point        2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGpoint    false  true   false  false  false  true    true
geo2          line         2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGline     false  true   false  false  false  true    true
geo3          lseg         2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGlseg     false  true   false  false  false  true    true
geo4          box          2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGbox      false  true   false  false  false  true    true
geo5          path         2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGpath     false  true   false  false  false  true    true
geo6          polygon      2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGpolygon  false  true   false  false  false  true    true
geo7          circle       2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.geometric.PGcircle   false  true   false  false  false  true    true
net1          cidr         2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
net2          inet         2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
net3          macaddr      2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
bit1          bit                  10          10      0  false   Yes           -7  BIT        java.lang.Boolean                   false  false  false  false  false  true    true
bit2          varbit               10          10      0  false   Yes         1111  OTHER      java.lang.Object                    false  false  false  false  false  true    true
uui1          uuid         2147483647  2147483647      0  false   Yes         1111  OTHER      java.util.UUID                      false  true   false  false  false  true    true
xml1          xml          2147483647  2147483647      0  false   Yes         2009  SQLXML     java.sql.SQLXML                     false  true   false  false  false  true    true
jso1          json         2147483647  2147483647      0  false   Yes         1111  OTHER      org.postgresql.util.PGobject        false  true   false  false  false  true    true
jso2          jsonb        2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
arr1          _int4                11          10      0  true    Yes         2003  ARRAY      java.sql.Array                      false  false  false  false  false  true    true
arr2          _bpchar               1           1      0  false   Yes         2003  ARRAY      java.sql.Array                      false  true   false  false  false  true    true
arr3          _int4                11          10      0  true    Yes         2003  ARRAY      java.sql.Array                      false  false  false  false  false  true    true
com1          complex      2147483647  2147483647      0  false   Yes         2002  STRUCT     java.lang.Object                    false  true   false  false  false  true    true
ran1          int4range    2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
ran2          int8range    2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
ran3          numrange     2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
ran4          tsrange      2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
ran5          tstzrange    2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true
ran6          daterange    2147483647  2147483647      0  false   Yes         1111  OTHER      java.lang.Object                    false  true   false  false  false  true    true

                 
-- ========================================= --
-- SQL Server --
-- ========================================= --

create table all_types_table (
  num1 bit,
  num2 tinyint,
  num3 smallint,
  num4 int,
  num5 bigint,
  --
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
  --
  num25 decimal(10,0),
  num26 decimal(10,2),
  num27 decimal(10,8),
  --
  num40 money,
  num41 smallmoney,
  --
  num50 float, -- ::= float(53)
  num51 float(1), -- single precision
  num52 float(24), -- single precision
  num53 float(25), -- double precision
  num54 float(53), -- double precision
  num55 real, -- ::= float(24)
  -- CHAR --------------------------------------------------------
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
  cha11 ntext, -- [DEPRECATED] 2^30 - 1 chars, unicode
  -- DATETIME --------------------------------------------------------
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
  tim3 time(7), -- accuracy: 100ns - 10µs
-- i1 interval MONTH,  
-- i2 interval MONTH(3),  
-- i3 interval YEAR,  
-- i4 interval YEAR(3),
-- i5 interval YEAR TO MONTH,   
-- i6 interval YEAR(3) TO MONTH,   
-- i7 interval DAY,   
-- i8 interval DAY(3),
-- i9 interval HOUR,  
-- i10 interval HOUR(3),
-- i11 interval MINUTE,  
-- i12 interval MINUTE(3),
-- i13 interval SECOND,  
-- i14 interval SECOND(5,2),
-- i15 interval DAY TO HOUR,   
-- i16 interval DAY(3) TO HOUR,
-- i17 interval DAY TO MINUTE,   
-- i18 interval DAY(3) TO MINUTE,   
-- i19 interval DAY TO SECOND,
-- i20 interval DAY(3) TO SECOND(4),
-- i21 interval HOUR TO MINUTE,
-- i22 interval HOUR(3) TO MINUTE,
-- i23 interval HOUR TO SECOND,
-- i24 interval HOUR(3) TO SECOND(4),
-- i25 interval MINUTE TO SECOND,
-- i26 interval MINUTE(3) TO SECOND(4),
  -- BINARY -----------------------------------------------
  bin1 binary(10), -- 1 to 8000 bytes
  bin2 varbinary(10), -- 1 to 8000 bytes
  bin3 varbinary(max), -- 2^31 - 1 bytes
  bin4 image, -- [DEPRECATED] 2^31 - 1 bytes 
  -- OTHER -----------------------------------------------
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

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                    Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver     ainc   case   curr   dwrit  ronly  search  writ 
------  ----------------  ----------  ----------  -----  ------  --------  ------  -------------  ----------------------------  -----  -----  -----  -----  -----  ------  -----
num1    bit                        1           1      0  false   Yes           -7  BIT            java.lang.Boolean             false  false  false  false  false  true    true 
num2    tinyint                    3           3      0  false   Yes           -6  TINYINT        java.lang.Short               false  false  false  false  false  true    true 
num3    smallint                   6           5      0  true    Yes            5  SMALLINT       java.lang.Short               false  false  false  false  false  true    true 
num4    int                       11          10      0  true    Yes            4  INTEGER        java.lang.Integer             false  false  false  false  false  true    true 
num5    bigint                    20          19      0  true    Yes           -5  BIGINT         java.lang.Long                false  false  false  false  false  true    true 
num10   decimal                   20          18      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num11   decimal                    3           1      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num12   decimal                    4           2      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num13   decimal                    5           3      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num14   decimal                    6           4      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num15   decimal                    7           5      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num16   decimal                    8           6      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num17   decimal                    9           7      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num18   decimal                   10           8      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num19   decimal                   11           9      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num20   decimal                   12          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num21   decimal                   19          17      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num22   decimal                   20          18      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num23   decimal                   21          19      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num25   decimal                   12          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num26   decimal                   12          10      2  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num27   decimal                   12          10      8  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  false  false  false  true    true 
num40   money                     21          19      4  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  true   false  false  true    true 
num41   smallmoney                12          10      4  true    Yes            3  DECIMAL        java.math.BigDecimal          false  false  true   false  false  true    true 
num50   float                     22          15      0  true    Yes            8  DOUBLE         java.lang.Double              false  false  false  false  false  true    true 
num51   real                      13           7      0  true    Yes            7  REAL           java.lang.Float               false  false  false  false  false  true    true 
num52   real                      13           7      0  true    Yes            7  REAL           java.lang.Float               false  false  false  false  false  true    true 
num53   float                     22          15      0  true    Yes            8  DOUBLE         java.lang.Double              false  false  false  false  false  true    true 
num54   float                     22          15      0  true    Yes            8  DOUBLE         java.lang.Double              false  false  false  false  false  true    true 
num55   real                      13           7      0  true    Yes            7  REAL           java.lang.Float               false  false  false  false  false  true    true 
cha1    char                      10          10      0  false   Yes            1  CHAR           java.lang.String              false  false  false  false  false  true    true 
cha2    varchar                   20          20      0  false   Yes           12  VARCHAR        java.lang.String              false  false  false  false  false  true    true 
cha3    varchar           2147483647  2147483647      0  false   Yes           12  VARCHAR        java.lang.String              false  false  false  false  false  true    true 
cha4    nchar                     10          10      0  false   Yes          -15  NCHAR          java.lang.String              false  false  false  false  false  true    true 
cha5    nvarchar                  20          20      0  false   Yes           -9  NVARCHAR       java.lang.String              false  false  false  false  false  true    true 
cha6    nvarchar          1073741823  1073741823      0  false   Yes           -9  NVARCHAR       java.lang.String              false  false  false  false  false  true    true 
cha10   text              2147483647  2147483647      0  false   Yes           -1  LONGVARCHAR    java.lang.String              false  false  false  false  false  false   true 
cha11   ntext             1073741823  1073741823      0  false   Yes          -16  LONGNVARCHAR   java.lang.String              false  false  false  false  false  false   true 
dat1    date                      10          10      0  false   Yes           91  DATE           java.sql.Date                 false  false  false  false  false  true    true 
dat2    datetime                  23          23      3  false   Yes           93  TIMESTAMP      java.sql.Timestamp            false  false  false  false  false  true    true 
dat3    datetime2                 27          27      7  false   Yes           93  TIMESTAMP      java.sql.Timestamp            false  false  false  false  false  true    true 
dat4    datetime2                 19          19      0  false   Yes           93  TIMESTAMP      java.sql.Timestamp            false  false  false  false  false  true    true 
dat5    datetime2                 27          27      7  false   Yes           93  TIMESTAMP      java.sql.Timestamp            false  false  false  false  false  true    true 
dat6    datetimeoffset            34          34      7  false   Yes         -155    ?            microsoft.sql.DateTimeOffset  false  false  false  false  false  true    true 
dat7    datetimeoffset            26          26      0  false   Yes         -155    ?            microsoft.sql.DateTimeOffset  false  false  false  false  false  true    true 
dat8    datetimeoffset            34          34      7  false   Yes         -155    ?            microsoft.sql.DateTimeOffset  false  false  false  false  false  true    true 
dat9    smalldatetime             16          16      0  false   Yes           93  TIMESTAMP      java.sql.Timestamp            false  false  false  false  false  true    true 
tim1    time                      16          16      7  false   Yes           92  TIME           java.sql.Time                 false  false  false  false  false  true    true 
tim2    time                       8           8      0  false   Yes           92  TIME           java.sql.Time                 false  false  false  false  false  true    true 
tim3    time                      16          16      7  false   Yes           92  TIME           java.sql.Time                 false  false  false  false  false  true    true 
bin1    binary                    20          10      0  false   Yes           -2  BINARY         [B                            false  false  false  false  false  true    true 
bin2    varbinary                 20          10      0  false   Yes           -3  VARBINARY      [B                            false  false  false  false  false  true    true 
bin3    varbinary         2147483647  2147483647      0  false   Yes           -3  VARBINARY      [B                            false  false  false  false  false  true    true 
bin4    image             2147483647  2147483647      0  false   Yes           -4  LONGVARBINARY  [B                            false  false  false  false  false  false   true 
hie1    hierarchyid             1784         892      0  false   Yes           -3  VARBINARY      [B                            false  false  false  false  false  false   true 
row1    timestamp                 16           8      0  false   No            -2  BINARY         [B                            false  false  false  false  true   true    false
uni1    uniqueidentifier          36          36      0  false   Yes            1  CHAR           java.lang.String              false  false  false  false  false  true    true 
xml1    xml               1073741823  1073741823      0  false   Yes          -16  LONGNVARCHAR   java.lang.String              false  true   false  false  false  false   true 
geog1   geography         2147483647  2147483647      0  false   Yes         -158    ?            [B                            false  false  false  false  false  false   true 
geog2   nvarchar          1073741823  1073741823      0  false   Yes           -9  NVARCHAR       java.lang.String              false  false  false  false  true   true    false
geom1   geometry          2147483647  2147483647      0  false   Yes         -157    ?            [B                            false  false  false  false  false  false   true 
geom2   nvarchar          1073741823  1073741823      0  false   Yes           -9  NVARCHAR       java.lang.String              false  false  false  false  true   true    false
  
                                 
-- ========================================= --
-- MariaDB --
-- ========================================= --

create table all_types_table (
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
  --
  float1 float, -- synonym: float4
  float2 float unsigned,
  float3 float(1),
  float04 float(24),
  float5 float(25), -- promotes to double
  float6 float(53),
--  float7 float(54),
  --  
  float08 real,
  float9 double precision,
  --
  double1 double, -- synonym: real, float8, double precision
  double2 double unsigned,
  --
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
  --
  dec30 decimal, -- synonym: numeric 
  dec31 decimal(10),
  dec32 decimal(10, 0), 
  --
  dec40 decimal(10, 2), -- 
--  dec41 decimal(10, -2), -- cannot have negative scale
  dec42 decimal(19, 4), -- BigDecimal
  -- CHAR ------------------------------------------------------------
  letter char(1),
  car_code char(6),
  name varchar(20),
  email tinytext, -- clob of up to 255 chars
  description text, -- clob of up to 65535 chars
  doc mediumtext, -- clob of up to 16M chars
  book longtext, -- clob of up to 4GB chars
  -- DATETIME ------------------------------------------------------------
  today date,
  now datetime,
  right_now timestamp,
  hour_dif time,
  initial_year year,
  -- BINARY ------------------------------------------------------------
  ip tinyblob,
  buffer blob,
  image mediumblob,
  program longblob,
  -- OTHER ------------------------------------------------------------
  color_value enum('yellow', 'red', 'blue', 'green', 'white'),
  included set('one', 'two', 'three', 'four', 'five', 'seven')
);

create view all_types_view as 
  select t.* from all_types_table t;

Column        Type                    Size  Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ
------------  ------------------  --------  ---------  -----  ------  --------  ------  -------------  -------------------------  -----  -----  -----  -----  -----  ------  ----
int10         TINYINT                    4          4      0  true    Yes           -6  TINYINT        java.lang.Integer          false  false  false  true   false  true    true
int11         TINYINT                    3          3      0  false   Yes           -6  TINYINT        java.lang.Integer          false  false  false  true   false  true    true
int12         SMALLINT                   6          6      0  true    Yes            5  SMALLINT       java.lang.Integer          false  false  false  true   false  true    true
int13         SMALLINT UNSIGNED          5          5      0  false   Yes            5  SMALLINT       java.lang.Integer          false  false  false  true   false  true    true
int14         MEDIUMINT                  9          9      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int15         MEDIUMINT UNSIGNED         8          8      0  false   Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int16         INTEGER                   11         11      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int17         INTEGER UNSIGNED          10         10      0  false   Yes            4  INTEGER        java.lang.Long             false  false  false  true   false  true    true
int18         BIGINT                    20         20      0  true    Yes           -5  BIGINT         java.lang.Long             false  false  false  true   false  true    true
int19         BIGINT UNSIGNED           20         20      0  false   Yes           -5  BIGINT         java.math.BigInteger       false  false  false  true   false  true    true
float1        FLOAT                     12         12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float2        FLOAT                     12         12     31  false   Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float3        FLOAT                     12         12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float04       FLOAT                     12         12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float5        DOUBLE                    22         22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float6        DOUBLE                    22         22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float08       DOUBLE                    22         22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float9        DOUBLE                    22         22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
double1       DOUBLE                    22         22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
double2       DOUBLE                    22         22     31  false   Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
dec1          DECIMAL                    2          1      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec2          DECIMAL                    3          2      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec3          DECIMAL                    4          3      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec4          DECIMAL                    5          4      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec5          DECIMAL                    6          5      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec6          DECIMAL                    7          6      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec7          DECIMAL                    8          7      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec8          DECIMAL                    9          8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec9          DECIMAL                   10          9      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec10         DECIMAL                   11         10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec11         DECIMAL                   12         11      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec12         DECIMAL                   13         12      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec13         DECIMAL                   14         13      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec14         DECIMAL                   15         14      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec15         DECIMAL                   16         15      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec16         DECIMAL                   17         16      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec17         DECIMAL                   18         17      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec18         DECIMAL                   19         18      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec19         DECIMAL                   20         19      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec20         DECIMAL                   21         20      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec30         DECIMAL                   11         10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec31         DECIMAL                   11         10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec32         DECIMAL                   11         10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec40         DECIMAL                   12         10      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
dec42         DECIMAL                   21         19      4  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  true   false  true    true
letter        CHAR                       1          4      0  true    Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
car_code      CHAR                       6         24      0  true    Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
name          VARCHAR                   20         80      0  true    Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
email         VARCHAR                  255       1020      0  true    Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
description   VARCHAR                65535     262140      0  true    Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
doc           VARCHAR             16777215   67108860      0  true    Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
book          VARCHAR                    0         -1      0  true    Yes           -1  LONGVARCHAR    java.lang.String           false  false  false  true   false  true    true
today         DATE                      10         10      0  true    Yes           91  DATE           java.sql.Date              false  true   false  true   false  true    true
now           DATETIME                  19         19      0  true    Yes           93  TIMESTAMP      java.sql.Timestamp         false  true   false  true   false  true    true
right_now     TIMESTAMP                 19         19      0  false   No            93  TIMESTAMP      java.sql.Timestamp         false  true   false  true   false  true    true
hour_dif      TIME                      10         10      0  true    Yes           92  TIME           java.sql.Time              false  true   false  true   false  true    true
initial_year  YEAR                       4          4      0  false   Yes           91  DATE           java.sql.Date              false  false  false  true   false  true    true
ip            TINYBLOB                 255        255      0  true    Yes           -3  VARBINARY      [B                         false  true   false  true   false  true    true
buffer        BLOB                   65535      65535      0  true    Yes           -3  VARBINARY      [B                         false  true   false  true   false  true    true
image         MEDIUMBLOB          16777215   16777215      0  true    Yes           -3  VARBINARY      [B                         false  true   false  true   false  true    true
program       LONGBLOB                  -1         -1      0  true    Yes           -4  LONGVARBINARY  [B                         false  true   false  true   false  true    true
color_value   CHAR                       6         24      0  true    Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
included      CHAR                      29        116      0  true    Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true

-- ========================================= --
-- MySQL --
-- ========================================= --

(same as above)

Column        Type                      Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ
------------  ------------------  ----------  ----------  -----  ------  --------  ------  -------------  -------------------------  -----  -----  -----  -----  -----  ------  ----
int10         TINYINT                      4           4      0  true    Yes           -6  TINYINT        java.lang.Integer          false  false  false  true   false  true    true
int11         TINYINT UNSIGNED             3           3      0  false   Yes           -6  TINYINT        java.lang.Integer          false  false  false  true   false  true    true
int12         SMALLINT                     6           6      0  true    Yes            5  SMALLINT       java.lang.Integer          false  false  false  true   false  true    true
int13         SMALLINT UNSIGNED            5           5      0  false   Yes            5  SMALLINT       java.lang.Integer          false  false  false  true   false  true    true
int14         MEDIUMINT                    9           9      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int15         MEDIUMINT UNSIGNED           8           8      0  false   Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int16         INT                         11          11      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  true   false  true    true
int17         INT UNSIGNED                10          10      0  false   Yes            4  INTEGER        java.lang.Long             false  false  false  true   false  true    true
int18         BIGINT                      20          20      0  true    Yes           -5  BIGINT         java.lang.Long             false  false  false  true   false  true    true
int19         BIGINT UNSIGNED             20          20      0  false   Yes           -5  BIGINT         java.math.BigInteger       false  false  false  true   false  true    true
float1        FLOAT                       12          12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float2        FLOAT UNSIGNED              12          12     31  false   Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float3        FLOAT                       12          12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float04       FLOAT                       12          12     31  true    Yes            7  REAL           java.lang.Float            false  false  false  true   false  true    true
float5        DOUBLE                      22          22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float6        DOUBLE                      22          22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float08       DOUBLE                      22          22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
float9        DOUBLE                      22          22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
double1       DOUBLE                      22          22     31  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
double2       DOUBLE UNSIGNED             22          22     31  false   Yes            8  DOUBLE         java.lang.Double           false  false  false  true   false  true    true
dec1          DECIMAL                      2           1      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec2          DECIMAL                      3           2      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec3          DECIMAL                      4           3      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec4          DECIMAL                      5           4      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec5          DECIMAL                      6           5      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec6          DECIMAL                      7           6      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec7          DECIMAL                      8           7      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec8          DECIMAL                      9           8      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec9          DECIMAL                     10           9      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec10         DECIMAL                     11          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec11         DECIMAL                     12          11      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec12         DECIMAL                     13          12      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec13         DECIMAL                     14          13      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec14         DECIMAL                     15          14      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec15         DECIMAL                     16          15      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec16         DECIMAL                     17          16      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec17         DECIMAL                     18          17      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec18         DECIMAL                     19          18      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec19         DECIMAL                     20          19      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec20         DECIMAL                     21          20      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec30         DECIMAL                     11          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec31         DECIMAL                     11          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec32         DECIMAL                     11          10      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec40         DECIMAL                     12          10      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
dec42         DECIMAL                     21          19      4  true    Yes            3  DECIMAL        java.math.BigDecimal       false  true   false  true   false  true    true
letter        CHAR                         1           1      0  false   Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
car_code      CHAR                         6           6      0  false   Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
name          VARCHAR                     20          20      0  false   Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
email         TINYTEXT                    63          63      0  false   Yes           12  VARCHAR        java.lang.String           false  false  false  true   false  true    true
description   TEXT                     16383       16383      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  false  false  true   false  true    true
doc           MEDIUMTEXT             4194303     4194303      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  false  false  true   false  true    true
book          LONGTEXT             536870911   536870911      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  false  false  true   false  true    true
today         DATE                        10          10      0  false   Yes           91  DATE           java.sql.Date              false  false  false  true   false  true    true
now           DATETIME                    19          19      0  false   Yes           93  TIMESTAMP      java.sql.Timestamp         false  false  false  true   false  true    true
right_now     TIMESTAMP                   19          19      0  false   Yes           93  TIMESTAMP      java.sql.Timestamp         false  false  false  true   false  true    true
hour_dif      TIME                        10          10      0  false   Yes           92  TIME           java.sql.Time              false  false  false  true   false  true    true
initial_year  YEAR                         4           4      0  false   Yes           91  DATE           java.sql.Date              false  false  false  true   false  true    true
ip            TINYBLOB                   255         255      0  false   Yes           -3  VARBINARY      [B                         false  true   false  true   false  true    true
buffer        BLOB                     65535       65535      0  false   Yes           -4  LONGVARBINARY  [B                         false  true   false  true   false  true    true
image         MEDIUMBLOB            16777215    16777215      0  false   Yes           -4  LONGVARBINARY  [B                         false  true   false  true   false  true    true
program       LONGBLOB            2147483647  2147483647      0  false   Yes           -4  LONGVARBINARY  [B                         false  true   false  true   false  true    true
color_value   CHAR                         6           6      0  false   Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true
included      CHAR                        29          29      0  false   Yes            1  CHAR           java.lang.String           false  false  false  true   false  true    true


-- ========================================= --
-- Sybase ASE --
-- ========================================= --

create table all_types_table (
  num1 bit,
  num2 tinyint,
  num3 unsigned tinyint,
  num4 smallint,
  num5 unsigned smallint,
  num6 int, -- synonym: integer
  num7 unsigned int,
  num8 bigint,
  num9 unsigned bigint,
  --
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
  --
  num25 decimal(10,0),
  num26 decimal(10,2),
  num27 decimal(10,8),
  --
  num40 money,
  num41 smallmoney,
  --
  num50 float, -- ::= double precision
  num51 float(1), -- double precision
  num52 float(24), -- double precision
  num53 float(25), -- double precision
  num54 float(48), -- double precision
  num55 real, -- ::= double precision
  num56 double precision, -- ::= double precision
  -- CHAR ----------------------------------------------------    
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
  cha15 sysname, -- equivalent to: varchar(30) not null
  -- DATETIME ----------------------------------------------------    
  dat1 date, -- just the day, no time info
  dat2 datetime, -- accuracy: 1 microsecond
  dat9 smalldatetime, -- accuray: 1 minute
  dat10 bigtime, -- accuracy: 1 microsecond
  dat11 bigdatetime, -- accuracy: 1 microsecond
  tim1 time, -- accuracy: 1ns
  -- tim2 time(1), -- accuracy: 100ms - 1s
  -- tim3 time(7) -- accuracy: 100ns - 10µs
  -- BINARY ----------------------------------------------------    
  bin1 binary(10), -- 1 to 8000 bytes
  bin2 varbinary(10), -- 1 to 8000 bytes
  bin4 image -- [DEPRECATED] 2^31 - 1 bytes 
             -- even if deprecated, there's no other BLOB-like type.
  -- bin5 uniqueidentifier, -- 16-byte GUID
);

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                     Size  Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case  curr   dwrit  ronly  search  writ
------  -----------------  ----------  ---------  -----  ------  --------  ------  -------------  -------------------------  -----  ----  -----  -----  -----  ------  ----
num1    bit                         1          0      0  false   No            -7  BIT            java.lang.Boolean          false  true  false  false  false  true    true
num2    tinyint                     3          3      0  true    No            -6  TINYINT        java.lang.Integer          false  true  false  false  false  true    true
num3    tinyint                     3          3      0  true    No            -6  TINYINT        java.lang.Integer          false  true  false  false  false  true    true
num4    smallint                    6          5      0  true    No             5  SMALLINT       java.lang.Integer          false  true  false  false  false  true    true
num5    unsigned smallint           6          5      0  false   No             5  SMALLINT       java.lang.Integer          false  true  false  false  false  true    true
num6    int                        11         10      0  true    No             4  INTEGER        java.lang.Integer          false  true  false  false  false  true    true
num7    unsigned int               11         10      0  false   No             4  INTEGER        java.lang.Long             false  true  false  false  false  true    true
num8    bigint                     20         19      0  true    No            -5  BIGINT         java.lang.Long             false  true  false  false  false  true    true
num9    unsigned bigint            20         20      0  false   No            -5  BIGINT         java.math.BigDecimal       false  true  false  false  false  true    true
num10   decimal                    20         18      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num11   decimal                     3          1      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num12   decimal                     4          2      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num13   decimal                     5          3      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num14   decimal                     6          4      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num15   decimal                     7          5      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num16   decimal                     8          6      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num17   decimal                     9          7      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num18   decimal                    10          8      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num19   decimal                    11          9      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num20   decimal                    12         10      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num21   decimal                    19         17      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num22   decimal                    20         18      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num23   decimal                    21         19      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num25   decimal                    12         10      0  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num26   decimal                    12         10      2  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num27   decimal                    12         10      8  true    No             3  DECIMAL        java.math.BigDecimal       false  true  false  false  false  true    true
num40   money                      21         19      4  true    No             3  DECIMAL        java.math.BigDecimal       false  true  true   false  false  true    true
num41   smallmoney                 12         10      4  true    No             3  DECIMAL        java.math.BigDecimal       false  true  true   false  false  true    true
num50   double precision           85         15      0  true    No             8  DOUBLE         java.lang.Double           false  true  false  false  false  true    true
num51   real                       46          7      0  true    No             7  REAL           java.lang.Float            false  true  false  false  false  true    true
num52   double precision           85         15      0  true    No             8  DOUBLE         java.lang.Double           false  true  false  false  false  true    true
num53   double precision           85         15      0  true    No             8  DOUBLE         java.lang.Double           false  true  false  false  false  true    true
num54   double precision           85         15      0  true    No             8  DOUBLE         java.lang.Double           false  true  false  false  false  true    true
num55   real                       46          7      0  true    No             7  REAL           java.lang.Float            false  true  false  false  false  true    true
num56   double precision           85         15      0  true    No             8  DOUBLE         java.lang.Double           false  true  false  false  false  true    true
cha1    char                       10          0      0  false   No             1  CHAR           java.lang.String           false  true  false  false  false  true    true
cha2    varchar                    20          0      0  false   No            12  VARCHAR        java.lang.String           false  true  false  false  false  true    true
cha3    unichar                    10          0      0  false   No             1  CHAR           java.lang.String           false  true  false  false  false  true    true
cha4    char                       10          0      0  false   No           -15  NCHAR          java.lang.String           false  true  false  false  false  ?       true
cha5    varchar                    20          0      0  false   No            -9  NVARCHAR       java.lang.String           false  true  false  false  false  ?       true
cha6    univarchar                 20          0      0  false   No            12  VARCHAR        java.lang.String           false  true  false  false  false  true    true
cha10   text               2147483647          0      0  false   No            -1  LONGVARCHAR    java.lang.String           false  true  false  false  false  true    true
cha12   unitext            1073741823          0      0  false   No            -1  LONGVARCHAR    java.lang.String           false  true  false  false  false  true    true
cha14   varchar                   255          0      0  false   No            12  VARCHAR        java.lang.String           false  true  false  false  false  true    true
cha15   varchar                    30          0      0  false   No            12  VARCHAR        java.lang.String           false  true  false  false  false  true    true
dat1    date                       10          0      0  false   No            91  DATE           java.sql.Date              false  true  false  false  false  true    true
dat2    datetime                   25          3      0  false   No            93  TIMESTAMP      java.sql.Timestamp         false  true  false  false  false  true    true
dat9    smalldatetime              25          0      0  false   No            93  TIMESTAMP      java.sql.Timestamp         false  true  false  false  false  true    true
dat10   bigtime                    15          6      6  false   No            92  TIME           java.sql.Time              false  true  false  false  false  true    true
dat11   bigdatetime                29          6      6  false   No            93  TIMESTAMP      java.sql.Timestamp         false  true  false  false  false  true    true
tim1    time                        8          3      0  false   No            92  TIME           java.sql.Time              false  true  false  false  false  true    true
bin1    binary                     20          0      0  false   No            -2  BINARY         [B                         false  true  false  false  false  true    true
bin2    varbinary                  20          0      0  false   No            -3  VARBINARY      [B                         false  true  false  false  false  true    true
bin4    image                       1          0      0  false   No            -4  LONGVARBINARY  [B                         false  true  false  false  false  true    true


-- ========================================= --
-- H2 --
-- ========================================= --

create table all_types_table (
  i1 int,       -- Integer
  i2 integer,   -- Integer
  i3 mediumint, -- Integer
  i4 int4,      -- Integer
  i5 signed,    -- Integer
  --
  i10 tinyint,  -- Byte
  --
  i20 smallint, -- Short
  i21 int2,     -- Short
  i22 year,     -- Short
  --
  i30 bigint,   -- Long
  i31 int8,     -- Long
  --
  -- i40 identity, -- Long
  --
  dec1 decimal(10, 2), -- BigDecimal
  dec2 decimal(19, 4), -- BigDecimal
  --
  dec3 numeric(10, 2), -- BigDecimal
  dec4 number(10, 2),  -- BigDecimal
  dec5 dec(10, 2),     -- BigDecimal
  --
  dou1 double,           -- Double
  dou2 double precision, -- Double
  dou3 float,            -- Double
  dou4 float8,           -- Double
  --
  rea1 real,   -- Float
  rea2 float4,  -- Float
  --- CHAR ------------------------------------------------
  vc1 varchar(100),                -- String
  vc2 longvarchar(100),            -- String
  vc3 varchar2(100),               -- String
  vc4 nvarchar(100),               -- String
  vc5 nvarchar2(100),              -- String
  vc6 varchar_casesensitive(100),  -- String
  vc7 varchar_ignorecase(100),     -- String
  --
  cha1 char(100),      -- String
  cha2 character(100), -- String
  cha3 nchar(100),     -- String
  --
  clo1 clob(1000000),       -- java.sql.Clob
  clo2 tinytext(1000000),   -- java.sql.Clob
  clo3 text(1000000),       -- java.sql.Clob
  clo4 mediumtext(1000000), -- java.sql.Clob
  clo5 longtext(1000000),   -- java.sql.Clob
  clo6 ntext(1000000),      -- java.sql.Clob
  clo7 nclob(1000000),       -- java.sql.Clob
  --- DATETIME ------------------------------------------------
  tim1 time, -- java.sql.Time
  dat1 date, -- java.sql.Date
  ts1 timestamp,     -- java.sql.Timestamp
  ts2 datetime,      -- java.sql.Timestamp
  ts3 smalldatetime, -- java.sql.Timestamp
  --tz4 timestamp with timezone, -- org.h2.api.TimestampWithTimeZone
  --- BINARY ------------------------------------------------
  bin1 binary(100),        -- byte[]; max 2GGB.
  bin2 varbinary(100),     -- byte[]; max 2GGB.
  bin3 longvarbinary(100), -- byte[]; max 2GGB.
  bin4 raw(100),           -- byte[]; max 2GGB.
  bin5 bytea(100),         -- byte[]; max 2GGB.
  --
  blo1 blob(1000000),       -- java.sql.Blob
  blo2 tinyblob(1000000),   -- java.sql.Blob
  blo3 mediumblob(1000000), -- java.sql.Blob
  blo4 longblob(1000000),   -- java.sql.Blob
  blo5 image(1000000),      -- java.sql.Blob
  blo6 oid(1000000),         -- java.sql.Blob
  --- OTHER ------------------------------------------------
  boo1 boolean, -- java.lang.Boolean
  boo2 bit,     -- java.lang.Boolean
  boo3 bool,    -- java.lang.Boolean
  --
  oth1 other, -- java.lang.Object
  --
  id1 uuid, -- java.util.UUID
  -- arr1 array, -- java.lang.Object[]
  geo1 geometry -- java.lang.Object / java.lang.String
);

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                      Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type  Class suggested by driver           ainc   case  curr   dwrit  ronly  search  writ
------  ------------------  ----------  ----------  -----  ------  --------  ------  ---------  ----------------------------------  -----  ----  -----  -----  -----  ------  ----
I1      INTEGER                     11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  true  false  false  false  true    true
I2      INTEGER                     11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  true  false  false  false  true    true
I3      INTEGER                     11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  true  false  false  false  true    true
I4      INTEGER                     11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  true  false  false  false  true    true
I5      INTEGER                     11          10      0  true    Yes            4  INTEGER    java.lang.Integer                   false  true  false  false  false  true    true
I10     TINYINT                      4           3      0  true    Yes           -6  TINYINT    java.lang.Byte                      false  true  false  false  false  true    true
I20     SMALLINT                     6           5      0  true    Yes            5  SMALLINT   java.lang.Short                     false  true  false  false  false  true    true
I21     SMALLINT                     6           5      0  true    Yes            5  SMALLINT   java.lang.Short                     false  true  false  false  false  true    true
I22     SMALLINT                     6           5      0  true    Yes            5  SMALLINT   java.lang.Short                     false  true  false  false  false  true    true
I30     BIGINT                      20          19      0  true    Yes           -5  BIGINT     java.lang.Long                      false  true  false  false  false  true    true
I31     BIGINT                      20          19      0  true    Yes           -5  BIGINT     java.lang.Long                      false  true  false  false  false  true    true
DEC1    DECIMAL                     10          10      2  true    Yes            3  DECIMAL    java.math.BigDecimal                false  true  false  false  false  true    true
DEC2    DECIMAL                     19          19      4  true    Yes            3  DECIMAL    java.math.BigDecimal                false  true  false  false  false  true    true
DEC3    DECIMAL                     10          10      2  true    Yes            3  DECIMAL    java.math.BigDecimal                false  true  false  false  false  true    true
DEC4    DECIMAL                     10          10      2  true    Yes            3  DECIMAL    java.math.BigDecimal                false  true  false  false  false  true    true
DEC5    DECIMAL                     10          10      2  true    Yes            3  DECIMAL    java.math.BigDecimal                false  true  false  false  false  true    true
DOU1    DOUBLE                      24          17      0  true    Yes            8  DOUBLE     java.lang.Double                    false  true  false  false  false  true    true
DOU2    DOUBLE                      24          17      0  true    Yes            8  DOUBLE     java.lang.Double                    false  true  false  false  false  true    true
DOU3    DOUBLE                      24          17      0  true    Yes            8  DOUBLE     java.lang.Double                    false  true  false  false  false  true    true
DOU4    DOUBLE                      24          17      0  true    Yes            8  DOUBLE     java.lang.Double                    false  true  false  false  false  true    true
REA1    REAL                        15           7      0  true    Yes            7  REAL       java.lang.Float                     false  true  false  false  false  true    true
REA2    REAL                        15           7      0  true    Yes            7  REAL       java.lang.Float                     false  true  false  false  false  true    true
VC1     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC2     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC3     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC4     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC5     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC6     VARCHAR                    100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
VC7     VARCHAR_IGNORECASE         100         100      0  true    Yes           12  VARCHAR    java.lang.String                    false  true  false  false  false  true    true
CHA1    CHAR                       100         100      0  true    Yes            1  CHAR       java.lang.String                    false  true  false  false  false  true    true
CHA2    CHAR                       100         100      0  true    Yes            1  CHAR       java.lang.String                    false  true  false  false  false  true    true
CHA3    CHAR                       100         100      0  true    Yes            1  CHAR       java.lang.String                    false  true  false  false  false  true    true
CLO1    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO2    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO3    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO4    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO5    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO6    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
CLO7    CLOB                   1000000     1000000      0  true    Yes         2005  CLOB       java.sql.Clob                       false  true  false  false  false  true    true
TIM1    TIME                         8           8      0  true    Yes           92  TIME       java.sql.Time                       false  true  false  false  false  true    true
DAT1    DATE                        10          10      0  true    Yes           91  DATE       java.sql.Date                       false  true  false  false  false  true    true
TS1     TIMESTAMP                   26          26      6  true    Yes           93  TIMESTAMP  java.sql.Timestamp                  false  true  false  false  false  true    true
TS2     TIMESTAMP                   26          26      6  true    Yes           93  TIMESTAMP  java.sql.Timestamp                  false  true  false  false  false  true    true
TS3     TIMESTAMP                   26          26      6  true    Yes           93  TIMESTAMP  java.sql.Timestamp                  false  true  false  false  false  true    true
BIN1    VARBINARY                  100         100      0  true    Yes           -3  VARBINARY  [B                                  false  true  false  false  false  true    true
BIN2    VARBINARY                  100         100      0  true    Yes           -3  VARBINARY  [B                                  false  true  false  false  false  true    true
BIN3    VARBINARY                  100         100      0  true    Yes           -3  VARBINARY  [B                                  false  true  false  false  false  true    true
BIN4    VARBINARY                  100         100      0  true    Yes           -3  VARBINARY  [B                                  false  true  false  false  false  true    true
BIN5    VARBINARY                  100         100      0  true    Yes           -3  VARBINARY  [B                                  false  true  false  false  false  true    true
BLO1    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BLO2    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BLO3    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BLO4    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BLO5    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BLO6    BLOB                   1000000     1000000      0  true    Yes         2004  BLOB       java.sql.Blob                       false  true  false  false  false  true    true
BOO1    BOOLEAN                      5           1      0  true    Yes           16  BOOLEAN    java.lang.Boolean                   false  true  false  false  false  true    true
BOO2    BOOLEAN                      5           1      0  true    Yes           16  BOOLEAN    java.lang.Boolean                   false  true  false  false  false  true    true
BOO3    BOOLEAN                      5           1      0  true    Yes           16  BOOLEAN    java.lang.Boolean                   false  true  false  false  false  true    true
OTH1    OTHER               2147483647  2147483647      0  true    Yes         1111  OTHER      java.lang.Object                    false  true  false  false  false  true    true
ID1     UUID                2147483647  2147483647      0  true    Yes           -2  BINARY     [B                                  false  true  false  false  false  true    true
GEO1    GEOMETRY            2147483647  2147483647      0  true    Yes         1111  OTHER      org.locationtech.jts.geom.Geometry  false  true  false  false  false  true    true


-- ========================================= --
-- HyperSQL --
-- ========================================= --

create table all_types_table (
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
  --  
  dec1 numeric(10, 2), -- BigDecimal
--  dec2 numeric(10, -2), -- cannot have negative scale
  dec3 decimal(10, 2), -- BigDecimal
  dec4 decimal(19, 4), -- BigDecimal
  --
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
  --
  float1 real, -- Double
  float2 float, -- Double
  float3 double, -- Double
  -- CHAR -------------------------------------------------------------  
  cha1 char, -- synonym: character; defaults to 1 char
--  cha2 char(0), -- minimum length is 1
  cha3 char(1), 
  cha4 char(100),
  --
--  vc1 varchar, -- length must be specified for varchar
--  vc2 varchar(0), -- minimum length is 1
  vc3 varchar(1), -- synonyms: character varying, char varying
  vc4 varchar(100),
  vc5 longvarchar, -- defaults to 16MB; by configuration can also be mapped as clob.
  vc6 longvarchar(1),
  vc7 longvarchar(100),
--  vc8 longvarchar(101K), -- no multipliers (K, M, G) allowed on char/varchar
  --
  clo1 clob, -- synonyms: character large object, char large object; defaults to 1GB
--  clo2 clob(0), -- minimum length is 1
  clo3 clob(1),
  clo4 clob(100),
  clo5 clob(102K),
  -- DATETIME -------------------------------------------------------------  
  dat0 date, -- cannot store time zone
  dat1 time, -- no time zone by default
  dat2 timestamp, -- no time zone by default
  dat3 time with time zone,
  dat4 timestamp with time zone,
  --
  dat10 time(0),
  dat11 time(3),
  dat12 time(6),
  dat13 time(9), -- up to nanoseconds precision (9 decimal places)
  --
  dat20 timestamp(0),
  dat21 timestamp(3),
  dat22 timestamp(9), -- up to nanoseconds precision (9 decimal places)
  -- BINARY -------------------------------------------------------------  
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
  bin24 blob(1M),
--  bin30 UUID -- a 128-bit binary value, very similar to binary(16) but with different comparator logic.
-- # UUID type not supported on version 2.2.9 of HSQLDB
  -- OTHER -------------------------------------------------------------  
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
  --
  oth0 other, -- stores any serializable java object
  --
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
  --
  arr0 int array
);

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                             Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type  Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ 
------  -------------------------  ----------  ----------  -----  ------  --------  ------  ---------  -------------------------  -----  -----  -----  -----  -----  ------  -----
INT1    TINYINT                             4           8      0  true    Yes           -6  TINYINT    java.lang.Integer          false  false  false  false  true   true    false
INT2    SMALLINT                            6          16      0  true    Yes            5  SMALLINT   java.lang.Integer          false  false  false  false  true   true    false
INT3    INTEGER                            11          32      0  true    Yes            4  INTEGER    java.lang.Integer          false  false  false  false  true   true    false
INT4    BIGINT                             20          64      0  true    Yes           -5  BIGINT     java.lang.Long             false  false  false  false  true   true    false
INT5    NUMERIC                           129         128      0  true    Yes            2  NUMERIC    java.math.BigDecimal       false  false  false  false  true   true    false
INT6    DECIMAL                           129         128      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
INT7    NUMERIC                            11          10      0  true    Yes            2  NUMERIC    java.math.BigDecimal       false  false  false  false  true   true    false
INT8    NUMERIC                            11          10      0  true    Yes            2  NUMERIC    java.math.BigDecimal       false  false  false  false  true   true    false
INT9    DECIMAL                            11          10      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
INT10   DECIMAL                            11          10      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC1    NUMERIC                            12          10      2  true    Yes            2  NUMERIC    java.math.BigDecimal       false  false  true   false  true   true    false
DEC3    DECIMAL                            12          10      2  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  true   false  true   true    false
DEC4    DECIMAL                            21          19      4  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  true   false  true   true    false
DEC11   DECIMAL                             2           1      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC12   DECIMAL                             3           2      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC13   DECIMAL                             4           3      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC14   DECIMAL                             5           4      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC15   DECIMAL                             6           5      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC16   DECIMAL                             7           6      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC17   DECIMAL                             8           7      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC18   DECIMAL                             9           8      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC19   DECIMAL                            10           9      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC20   DECIMAL                            11          10      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC21   DECIMAL                            12          11      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC22   DECIMAL                            13          12      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC23   DECIMAL                            14          13      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC24   DECIMAL                            15          14      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC25   DECIMAL                            16          15      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC26   DECIMAL                            17          16      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC27   DECIMAL                            18          17      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC28   DECIMAL                            19          18      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC29   DECIMAL                            20          19      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
DEC30   DECIMAL                            21          20      0  true    Yes            3  DECIMAL    java.math.BigDecimal       false  false  false  false  true   true    false
FLOAT1  DOUBLE                             23          64      0  true    Yes            8  DOUBLE     java.lang.Double           false  false  false  false  true   true    false
FLOAT2  DOUBLE                             23          64      0  true    Yes            8  DOUBLE     java.lang.Double           false  false  false  false  true   true    false
FLOAT3  DOUBLE                             23          64      0  true    Yes            8  DOUBLE     java.lang.Double           false  false  false  false  true   true    false
CHA1    CHARACTER                           1           1      0  false   Yes            1  CHAR       java.lang.String           false  true   false  false  true   true    false
CHA3    CHARACTER                           1           1      0  false   Yes            1  CHAR       java.lang.String           false  true   false  false  true   true    false
CHA4    CHARACTER                         100         100      0  false   Yes            1  CHAR       java.lang.String           false  true   false  false  true   true    false
VC3     VARCHAR                             1           1      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
VC4     VARCHAR                           100         100      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
VC5     VARCHAR                      16777216    16777216      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
VC6     VARCHAR                             1           1      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
VC7     VARCHAR                           100         100      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
CLO1    CLOB                       1073741824  1073741824      0  false   Yes         2005  CLOB       java.sql.Clob              false  true   false  false  true   false   false
CLO3    CLOB                                1           1      0  false   Yes         2005  CLOB       java.sql.Clob              false  true   false  false  true   false   false
CLO4    CLOB                              100         100      0  false   Yes         2005  CLOB       java.sql.Clob              false  true   false  false  true   false   false
CLO5    CLOB                           104448      104448      0  false   Yes         2005  CLOB       java.sql.Clob              false  true   false  false  true   false   false
DAT0    DATE                               10          10      0  false   Yes           91  DATE       java.sql.Date              false  false  false  false  true   true    false
DAT1    TIME                                8           8      0  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT2    TIMESTAMP                          26          26      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp         false  false  false  false  true   true    false
DAT3    TIME WITH TIME ZONE                 8           8      0  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT4    TIMESTAMP WITH TIME ZONE           26          26      6  false   Yes           93  TIMESTAMP  java.sql.Timestamp         false  false  false  false  true   true    false
DAT10   TIME                                8           8      0  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT11   TIME                               12          12      3  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT12   TIME                               15          15      6  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT13   TIME                               18          18      9  false   Yes           92  TIME       java.sql.Time              false  false  false  false  true   true    false
DAT20   TIMESTAMP                          19          19      0  false   Yes           93  TIMESTAMP  java.sql.Timestamp         false  false  false  false  true   true    false
DAT21   TIMESTAMP                          23          23      3  false   Yes           93  TIMESTAMP  java.sql.Timestamp         false  false  false  false  true   true    false
DAT22   TIMESTAMP                          29          29      9  false   Yes           93  TIMESTAMP  java.sql.Timestamp         false  false  false  false  true   true    false
BIN0    BINARY                              1           1      0  false   Yes           -2  BINARY     [B                         false  false  false  false  true   true    false
BIN2    BINARY                              1           1      0  false   Yes           -2  BINARY     [B                         false  false  false  false  true   true    false
BIN3    BINARY                            100         100      0  false   Yes           -2  BINARY     [B                         false  false  false  false  true   true    false
BIN12   VARBINARY                           1           1      0  false   Yes           -3  VARBINARY  [B                         false  false  false  false  true   true    false
BIN13   VARBINARY                         100         100      0  false   Yes           -3  VARBINARY  [B                         false  false  false  false  true   true    false
BIN20   BLOB                       1073741824  1073741824      0  false   Yes         2004  BLOB       java.sql.Blob              false  false  false  false  true   false   false
BIN22   BLOB                                1           1      0  false   Yes         2004  BLOB       java.sql.Blob              false  false  false  false  true   false   false
BIN23   BLOB                              100         100      0  false   Yes         2004  BLOB       java.sql.Blob              false  false  false  false  true   false   false
BIN24   BLOB                          1048576     1048576      0  false   Yes         2004  BLOB       java.sql.Blob              false  false  false  false  true   false   false
BOOL0   BOOLEAN                             5           0      0  false   Yes           16  BOOLEAN    java.lang.Boolean          false  false  false  false  true   true    false
BIT0    BIT                                 1           1      0  false   Yes           -7  BIT        [B                         false  false  false  false  true   true    false
BIT2    BIT                                 1           1      0  false   Yes           -7  BIT        [B                         false  false  false  false  true   true    false
BIT3    BIT                               100         100      0  false   Yes           -7  BIT        [B                         false  false  false  false  true   true    false
BIT12   BIT VARYING                         1           1      0  false   Yes           -7  BIT        [B                         false  false  false  false  true   true    false
BIT13   BIT VARYING                       100         100      0  false   Yes           -7  BIT        [B                         false  false  false  false  true   true    false
OTH0    OTHER                               0           0      0  false   Yes         1111  OTHER      java.lang.Object           false  false  false  false  true   false   false
ITV0    INTERVAL YEAR                       3           3      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV1    INTERVAL MONTH                      3           3      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV2    INTERVAL DAY                        3           3      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV3    INTERVAL HOUR                       3           3      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV4    INTERVAL MINUTE                     3           3      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV5    INTERVAL SECOND                    10          10      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV6    INTERVAL YEAR TO MONTH              6           6      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV7    INTERVAL DAY TO HOUR                6           6      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV8    INTERVAL DAY TO SECOND             19          19      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ITV9    INTERVAL MINUTE TO SECOND          13          13      0  false   Yes           12  VARCHAR    java.lang.String           false  true   false  false  true   true    false
ARR0    INTEGER ARRAY                   12295           0      0  false   Yes         2003  ARRAY      java.sql.Array             false  false  false  false  true   true    false


-- ========================================= --
-- Derby --
-- ========================================= --

create table all_types_table (
-- numeric, decimal
  num1 numeric(2), -- java.math.BigDecimal -- default scale is 0
  num2 numeric(4),
  num3 numeric(9),
  num4 numeric(18),
  num5 numeric(31), -- max precision is 31
  num7 numeric(6,2),
  num8 numeric(10,2),
  num9 numeric, -- numeric(5,0) -- default precision is 5
  --
  num10 smallint,
  num11 integer, -- INTEGER | INT
  num12 bigint,
  --
  num20 float, -- FLOAT(53) -- java.lang.Double
  num21 float(23), -- REAL -- java.lang.Float
  num22 float(24), -- DOUBLE -- java.lang.Double
  num23 real, -- java.lang.Float
  num24 double, -- DOUBLE | DOUBLE PRECISION -- java.lang.Double
  -- CHAR ------------------------------------------------------
  cha1 char(10), -- CHAR | CHARACTER -- max 254 chars
  cha2 varchar(20), -- VARCHAR | CHAR VARYING | CHARACTER VARYING -- max 32672 chars 
  cha3 long varchar, -- no size allowed -- max 32700
  cha4 clob, -- CLOB | CHARACTER LARGE OBJECT -- max 2,147,483,647 chars
  -- DATETIME ------------------------------------------------------
  dat1 date, -- date without time
  dat2 time, -- time wihtout date -- seconds precision
  dat3 timestamp, -- date and time -- nanosecond precision
  -- BINARY ------------------------------------------------------
  bin1 blob, -- BLOB | BINARY LARGE OBJECT -- max 2,147,483,647 chars
  bin2 varchar(1000) for bit data, -- { VARCHAR | CHAR VARYING | CHARACTER VARYING } (length) FOR BIT DATA -- max 32672 bytes
  bin3 long varchar for bit data, -- no size allowed -- max 32700 chars
  bin4 char(10) for bit data, -- { CHAR | CHARACTER } [(length)] FOR BIT DATA -- max 254 bytes
  -- OTHER ------------------------------------------------------
  boo1 boolean
  -- xml1 xml -- not supported, needs more research
);

create view all_types_view as 
  select t.* from all_types_table t;

Column  Type                             Size   Precision  Scale  Signed  Nullable  JDBC #  JDBC type      Class suggested by driver  ainc   case   curr   dwrit  ronly  search  writ 
------  -------------------------  ----------  ----------  -----  ------  --------  ------  -------------  -------------------------  -----  -----  -----  -----  -----  ------  -----
NUM1    DECIMAL                             3           2      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM2    DECIMAL                             5           4      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM3    DECIMAL                            10           9      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM4    DECIMAL                            19          18      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM5    DECIMAL                            32          31      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM7    DECIMAL                             8           6      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM8    DECIMAL                            12          10      2  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM9    DECIMAL                             6           5      0  true    Yes            3  DECIMAL        java.math.BigDecimal       false  false  false  false  true   true    false
NUM10   SMALLINT                            6           5      0  true    Yes            5  SMALLINT       java.lang.Integer          false  false  false  false  true   true    false
NUM11   INTEGER                            11          10      0  true    Yes            4  INTEGER        java.lang.Integer          false  false  false  false  true   true    false
NUM12   BIGINT                             20          19      0  true    Yes           -5  BIGINT         java.lang.Long             false  false  false  false  true   true    false
NUM20   DOUBLE                             24          15      0  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  false  true   true    false
NUM21   REAL                               15           7      0  true    Yes            7  REAL           java.lang.Float            false  false  false  false  true   true    false
NUM22   DOUBLE                             24          15      0  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  false  true   true    false
NUM23   REAL                               15           7      0  true    Yes            7  REAL           java.lang.Float            false  false  false  false  true   true    false
NUM24   DOUBLE                             24          15      0  true    Yes            8  DOUBLE         java.lang.Double           false  false  false  false  true   true    false
CHA1    CHAR                               10          10      0  false   Yes            1  CHAR           java.lang.String           false  true   false  false  true   true    false
CHA2    VARCHAR                            20          20      0  false   Yes           12  VARCHAR        java.lang.String           false  true   false  false  true   true    false
CHA3    LONG VARCHAR                    32700       32700      0  false   Yes           -1  LONGVARCHAR    java.lang.String           false  true   false  false  true   true    false
CHA4    CLOB                       2147483647  2147483647      0  false   Yes         2005  CLOB           java.sql.Clob              false  true   false  false  true   true    false
DAT1    DATE                               10          10      0  false   Yes           91  DATE           java.sql.Date              false  false  false  false  true   true    false
DAT2    TIME                                8           8      0  false   Yes           92  TIME           java.sql.Time              false  false  false  false  true   true    false
DAT3    TIMESTAMP                          29          29      9  false   Yes           93  TIMESTAMP      java.sql.Timestamp         false  false  false  false  true   true    false
BIN1    BLOB                       2147483647  2147483647      0  false   Yes         2004  BLOB           java.sql.Blob              false  false  false  false  true   true    false
BIN2    VARCHAR FOR BIT DATA             2000        1000      0  false   Yes           -3  VARBINARY      byte[]                     false  false  false  false  true   true    false
BIN3    LONG VARCHAR FOR BIT DATA       65400       32700      0  false   Yes           -4  LONGVARBINARY  byte[]                     false  false  false  false  true   true    false
BIN4    CHAR FOR BIT DATA                  20          10      0  false   Yes           -2  BINARY         byte[]                     false  false  false  false  true   true    false
BOO1    BOOLEAN                             5           1      0  false   Yes           16  BOOLEAN        java.lang.Boolean          false  false  false  false  true   true    false

  