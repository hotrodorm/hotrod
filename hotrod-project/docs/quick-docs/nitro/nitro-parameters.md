# Nitro Parameters

Most likely your query will need parameters for its execution. You can add parameters using the `<parameter>` tag that takes the form:

```xml
<parameter name="clientId" java-type="Integer" />
```

As in:

```xml
<select method="findClientsActiveAccounts" vo="ActiveAccountVO">
  <parameter name="clientId" java-type="Integer" />
  select *
  from account a
  where a.id = #{clientId} and a.active = 1 
</select>
```

The `<parameter>` tag above specifies:

 - There will be a Java parameter called `clientId` in the method.
 - This parameter will be of type `java.lang.Integer`.
 
The `<select>` tag can include multiple `<parameter>` tags, and each parameter can be applied multiple times in a query.

The resulting Java method for the `<select>` tag above will be:

```java
public List<ActiveAccountVO> findClientsActiveAccounts(Integer clientId) {
  ...
}
```

For analysis purposes, during the code generation, HotRod replaces each parameter with a sample SQL value of the corresponding type. This 
sample value depends on the corresponding JDBC type of the parameter and the specific database engine.

## Location

Parameters can be used in any place of the SQL statement. This includes:

 - In the `WHERE` clause
 - In the `SELECT` clause
 - In subqueries
 - In any other place the specific JDBC driver and the database engine allows it. Please note the engine usually limits the location of JDBC parameters.

## The `<complement>` Tag

The `<complement>` tag usage depends on the SQL processor used:

 - When using the traditional `create-view` processor the `<complement>` tag is used to enclose sections of `WHERE` clauses with
   parameters. It's also used to enclose Dynamic SQL tags.
 - When using the modern `result-set` processor there's no need to enclose parameters anymore. The `<complement>` tag is still used 
   to enclose Dynamic SQL tags.

To select a query processor see [Select Generation](../config/tags/select-generation.md) configuration.

**Note**: Unlike `<select>` queries general purpose queries, that use the tag `<query>`, do no require the use of the `<complement>` tag, at all.

## The JDBC Type

In addition to its Java type HotRod needs to determine the its JDBC type; usually the JDBC type of a parameter is inferred from the `java-type` 
attribute. It can also be explicitly indicated using the `jdbc-type` attribute in the &lt;parameter tag, as in:

```xml
<parameter name="clientId" java-type="Integer" jdbc-type="INTEGER" />
```

For the full list of JDBC types see the [java.sql.Types](https://docs.oracle.com/javase/8/docs/api/java/sql/Types.html) class documentation.

For parameters with typical Java types it's not necessary to include the `jdbc-type` attribute. HotRod assigns it according to the following table:

| `java-type` | `jdbc-type` |
| -- | -- |
| `Byte` or `java.lang.Byte`       | TINYINT                 | 
| `Short` or `java.lang.Short`     | SMALLINT                | 
| `Integer` or `java.lang.Integer` | INTEGER                 | 
| `Long` or `java.lang.Long`       | BIGINT                  | 
| `Float` or `java.lang.Float`    | REAL                    | 
| `Double` or `java.lang.Double`   | DOUBLE                  | 
| `java.math.BigInteger`         | DECIMAL                 |  
| `java.math.BigDecimal`         | DECIMAL                 | 
| `Char` or `java.lang.Char`       | CHAR                    | 
| `String` or `java.lang.String`   | VARCHAR                 | 
| `java.util.Date`               | TIMESTAMP               | 
| `java.sql.Date`                | DATE                    | 
| `java.sql.Timestamp`           | TIMESTAMP               | 
| `java.sql.Time`                | TIME                    | 
| `java.time.LocalDateTime`      | TIMESTAMP               | 
| `java.time.OffsetDateTime`     | TIMESTAMP_WITH_TIMEZONE | 
| `java.time.ZonedDateTime`      | TIMESTAMP_WITH_TIMEZONE | 
| `java.time.LocalDate`          | DATE                    | 
| `java.time.LocalTime`          | TIME                    | 
| `Boolean` or `java.lang.Boolean` | BOOLEAN                 |
| `byte[]`                       | BLOB                    | 


If the `java-type` of a parameter is not covered in this table, the developer needs to specify the `jdbc-type` explicitly. Typically this attribute is
used only for uncommon or exotic parameter types such as UUIDs, geometry types, arrays, Objects, etc.

## The Sample SQL Value

When HotRod is analyzing the SQL statement it will replace each parameter with a sample value. This value is auto-generated according to
the `jdbc-type` as discussed before or it can be directly specified using the `sample-sql-value` attribute in the &lt;parameter> 
tag, as in:

    <parameter name="clientId" java-type="Integer" sample-sql-value="123" />

The value in the `sample-sql-value` attribute is a database value, not a Java value. It will be added to the SQL statement and will be parsed and evaluated by the database engine used during the code generation. The value can be as simple as `"123"` or maybe a more complex value such as `"convert(timestamp, '2001-01-01 12:34:56')"`. In any case, it needs to evaluate to a valid database data type during the code generation.

In any case, this attribute is rarely used; however, it can come in handy in the case the developer needs to use a parameter in the `SELECT` clause that needs to be correctly
typed when assemble the corresponding VO for the query, in the presence of uncommon or exotic parameter types if these types are not covered in the table below.

Finally, please note these value serve for analysis purposes of the SQL statement during code generation only, is not present in the generated code, and is never used when running the application.

More often than not HotRod assigns the *SQL Sample Value* by default, according to the following table:

    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    Oracle     TINYINT                 cast(1 as number(2))                              
               SMALLINT                cast(1 as number(4))                              
               INTEGER                 cast(1 as number(9))                              
               BIGINT                  cast(1 as number(18))                             
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as float)                                  
               DOUBLE                  cast(1 as double precision)                       
               DECIMAL                 cast(1 as decimal(10))                            
               NUMERIC                 cast(1 as numeric(10))                            
               CHAR                    cast('a' as char(1))                              
               NCHAR                   cast(N'a' as nchar(1))                            
               VARCHAR                 cast('a' as varchar2(1))                          
               NVARCHAR                cast(N'a' as nvarchar2(1))                        
               LONGVARCHAR             N/A                                               
               LONGNVARCHAR            N/A                                               
               CLOB                    empty_clob()                                      
               NCLOB                   N/A                                               
               DATE                    date '2001-01-01'                                 
               TIME                    N/A                                               
               TIMESTAMP               timestamp '2001-01-01 12:34:56'                   
               TIMESTAMP_WITH_TIMEZONE timestamp '2001-01-01 12:34:56 -08:00'            
               BLOB                    empty_blob()                                      
               BINARY                  N/A                                               
               VARBINARY               N/A                                               
               LONGVARBINARY           N/A                                               
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    DB2        TINYINT                 cast(1 as smallint)                               
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as real)                                   
               DOUBLE                  cast(1 as double)                                 
               DECIMAL                 cast(1 as decimal(10))                            
               NUMERIC                 cast(1 as numeric(10))                            
               CHAR                    cast('a' as char(1))                              
               NCHAR                   cast(N'a' as nchar(1))                            
               VARCHAR                 cast('a' as varchar(1))                           
               NVARCHAR                cast(N'a' as nvarchar(1))                         
               LONGVARCHAR             N/A                                               
               LONGNVARCHAR            N/A                                               
               CLOB                    cast('a' as clob(1))                              
               NCLOB                   N/A                                               
               DATE                    date '2001-01-01'                                 
               TIME                    time '12:34:56'                                   
               TIMESTAMP               timestamp '2001-01-01 12:34:56'                   
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    blob('a')                                         
               BINARY                  N/A                                               
               VARBINARY               N/A                                               
               LONGVARBINARY           N/A                                               
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    PostgreSQL TINYINT                 1::smallint                                       
               SMALLINT                1::smallint                                       
               INTEGER                 1::int                                            
               BIGINT                  1::bigint                                         
               REAL                    1.0::float                                        
               FLOAT                   1.0::double precision                             
               DOUBLE                  1.0::double precision                             
               DECIMAL                 1.0::decimal                                      
               NUMERIC                 1::numeric                                        
               CHAR                    'a'::char                                         
               NCHAR                   'a'::char                                         
               VARCHAR                 'a'::varchar                                      
               NVARCHAR                'a'::varchar                                      
               LONGVARCHAR             'a'::text                                         
               LONGNVARCHAR            'a'::text                                         
               CLOB                    'a'::text                                         
               NCLOB                   'a'::text                                         
               DATE                    date '2001-10-05'                                 
               TIME                    '04:05'::time                                     
               TIMESTAMP               timestamp '2004-10-19 10:23:54'                   
               TIMESTAMP_WITH_TIMEZONE timestamptz '2004-10-19 10:23:54+02'              
               BLOB                    E'ab'::bytea                                      
               BINARY                  E'ab'::bytea                                      
               VARBINARY               E'ab'::bytea                                      
               LONGVARBINARY           E'ab'::bytea                                      
               BOOLEAN                 true                                              
               SQLXML                  xmlcomment('a')                                   
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    SQL Server TINYINT                 cast(1 as tinyint)                                
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as float)                                  
               DOUBLE                  cast(1 as float)                                  
               DECIMAL                 cast(1 as decimal(10))                            
               NUMERIC                 cast(1 as numeric(10))                            
               CHAR                    cast('a' as char(1))                              
               NCHAR                   cast(N'a' as nchar(1))                            
               VARCHAR                 cast('a' as varchar(1))                           
               NVARCHAR                cast(N'a' as nvarchar(1))                         
               LONGVARCHAR             N/A                                               
               LONGNVARCHAR            N/A                                               
               CLOB                    cast('a' as text)                                 
               NCLOB                   N/A                                               
               DATE                    convert(date, '2001-01-01 12:34:56')              
               TIME                    convert(time, '2001-01-01 12:34:56')              
               TIMESTAMP               convert(timestamp, '2001-01-01 12:34:56')         
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    cast('a' as binary)                               
               BINARY                  N/A                                               
               VARBINARY               N/A                                               
               LONGVARBINARY           N/A                                               
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    MariaDB    TINYINT                 1                                                 
               SMALLINT                1                                                 
               INTEGER                 1                                                 
               BIGINT                  1                                                 
               REAL                    1.2                                               
               FLOAT                   1.2                                               
               DOUBLE                  1.2                                               
               DECIMAL                 1.2                                               
               NUMERIC                 1.2                                               
               CHAR                    'a'                                               
               NCHAR                   'a'                                               
               VARCHAR                 'a'                                               
               NVARCHAR                'a'                                               
               LONGVARCHAR             'a'                                               
               LONGNVARCHAR            'a'                                               
               CLOB                    'a'                                               
               NCLOB                   'a'                                               
               DATE                    date '2001-01-01'                                 
               TIME                    time '12:34:56'                                   
               TIMESTAMP               timestamp '2001-01-01 12:34:56'                   
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    b'01'                                             
               BINARY                  b'01'                                             
               VARBINARY               b'01'                                             
               LONGVARBINARY           b'01'                                             
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    MySQL      TINYINT                 1                                                 
               SMALLINT                1                                                 
               INTEGER                 1                                                 
               BIGINT                  1                                                 
               REAL                    1.2                                               
               FLOAT                   1.2                                               
               DOUBLE                  1.2                                               
               DECIMAL                 1.2                                               
               NUMERIC                 1.2                                               
               CHAR                    'a'                                               
               NCHAR                   'a'                                               
               VARCHAR                 'a'                                               
               NVARCHAR                'a'                                               
               LONGVARCHAR             'a'                                               
               LONGNVARCHAR            'a'                                               
               CLOB                    'a'                                               
               NCLOB                   'a'                                               
               DATE                    date '2001-01-01'                                 
               TIME                    time '12:34:56'                                   
               TIMESTAMP               timestamp '2001-01-01 12:34:56'                   
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    b'01'                                             
               BINARY                  b'01'                                             
               VARBINARY               b'01'                                             
               LONGVARBINARY           b'01'                                             
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    Sybase ASE TINYINT                 cast(1 as tinyint)                                
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as double precision)                       
               DOUBLE                  cast(1 as double precision)                       
               DECIMAL                 cast(1 as decimal(10))                            
               NUMERIC                 cast(1 as numeric(10))                            
               CHAR                    cast('a' as char(1))                              
               NCHAR                   cast(N'a' as nchar(1))                            
               VARCHAR                 cast('a' as varchar(1))                           
               NVARCHAR                cast(N'a' as nvarchar(1))                         
               LONGVARCHAR             N/A                                               
               LONGNVARCHAR            N/A                                               
               CLOB                    cast('a' as text)                                 
               NCLOB                   N/A                                               
               DATE                    convert(date, '2001-01-01 12:34:56')              
               TIME                    convert(time, '2001-01-01 12:34:56')              
               TIMESTAMP               convert(datetime, '2001-01-01 12:34:56')          
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    cast('a' as binary)                               
               BINARY                  N/A                                               
               VARBINARY               N/A                                               
               LONGVARBINARY           N/A                                               
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    H2         TINYINT                 cast(1 as tinyint)                                
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as double)                                 
               DOUBLE                  cast(1 as double)                                 
               DECIMAL                 cast(1 as decimal)                                
               NUMERIC                 cast(1 as numeric)                                
               CHAR                    cast('a' as char)                                 
               NCHAR                   cast('a' as char)                                 
               VARCHAR                 cast('a' as varchar)                              
               NVARCHAR                cast('a' as varchar)                              
               LONGVARCHAR             cast('a' as clob)                                 
               LONGNVARCHAR            cast('a' as clob)                                 
               CLOB                    cast('a' as clob)                                 
               NCLOB                   cast('a' as clob)                                 
               DATE                    date '2001-10-05'                                 
               TIME                    time '04:05:06'                                   
               TIMESTAMP               timestamp '2004-10-19 10:23:54'                   
               TIMESTAMP_WITH_TIMEZONE timestamp with time zone '2004-10-19 10:23:54+02' 
               BLOB                    cast(x'01ab' as blob)                             
               BINARY                  cast(x'01ab' as blob)                             
               VARBINARY               cast(x'01ab' as blob)                             
               LONGVARBINARY           cast(x'01ab' as blob)                             
               BOOLEAN                 true                                              
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    HyperSQL   TINYINT                 cast(1 as tinyint)                                
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as double)                                 
               FLOAT                   cast(1 as double)                                 
               DOUBLE                  cast(1 as double)                                 
               DECIMAL                 cast(1 as decimal)                                
               NUMERIC                 cast(1 as numeric)                                
               CHAR                    cast('a' as char)                                 
               NCHAR                   cast('a' as char)                                 
               VARCHAR                 cast('a' as varchar(1))                           
               NVARCHAR                cast('a' as varchar(1))                           
               LONGVARCHAR             cast('a' as clob)                                 
               LONGNVARCHAR            cast('a' as clob)                                 
               CLOB                    cast('a' as clob)                                 
               NCLOB                   cast('a' as clob)                                 
               DATE                    date '2001-10-05'                                 
               TIME                    time '04:05:06'                                   
               TIMESTAMP               timestamp '2004-10-19 10:23:54'                   
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    x'01'                                             
               BINARY                  x'01'                                             
               VARBINARY               x'01'                                             
               LONGVARBINARY           x'01'                                             
               BOOLEAN                 true                                              
               SQLXML                  N/A                                               
    
    Database   jdbc-type               Sample SQL Value                                            
    ---------- ----------------------- ------------------------------------------------- 
    Derby      TINYINT                 cast(1 as smallint)                               
               SMALLINT                cast(1 as smallint)                               
               INTEGER                 cast(1 as int)                                    
               BIGINT                  cast(1 as bigint)                                 
               REAL                    cast(1 as real)                                   
               FLOAT                   cast(1 as double)                                 
               DOUBLE                  cast(1 as double)                                 
               DECIMAL                 cast(1 as decimal(10))                            
               NUMERIC                 cast(1 as numeric(10))                            
               CHAR                    cast('a' as char(1))                              
               NCHAR                   N/A                                               
               VARCHAR                 cast('a' as varchar(1))                           
               NVARCHAR                N/A                                               
               LONGVARCHAR             N/A                                               
               LONGNVARCHAR            N/A                                               
               CLOB                    cast('a' as clob(1))                              
               NCLOB                   N/A                                               
               DATE                    date('2001-01-01')                                
               TIME                    time('12:34:56')                                  
               TIMESTAMP               timestamp('2001-01-01 12:34:56')                  
               TIMESTAMP_WITH_TIMEZONE N/A                                               
               BLOB                    x'1a'                                             
               BINARY                  N/A                                               
               VARBINARY               N/A                                               
               LONGVARBINARY           N/A                                               
               BOOLEAN                 N/A                                               
               SQLXML                  N/A                                               
     
