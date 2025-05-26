# Nitro Parameters

Most likely your query will need one or more parameters for its execution. These can be
directly applied in the query, can be used to govern DynamicSQL sections, or both.

To include parameters in your query use the `<parameter>` tag.


## The `<parameter>` Tag

A `<parameter>` tag defines a parameter that will be received by the query. A query can
define zero, one, or more parameters. The parameters can be plain values or complex
objects with properties, arrays, or any combination of them.

The example shown below includes two parameter definitions:

```xml
  <select method="findClientsActiveAccounts" vo="ActiveAccount">
    <parameter name="clientId" java-type="Integer" />
    <parameter name="type" java-type="String" />
    SELECT *
    FROM account
    WHERE id = #{clientId}
      AND type = #{type}
      AND active = 1 
  </select>
```

In this example:

 - The query has two parameters (`clientId` and `type`) that will be provided when calling the Java method in the DAO.
 - The parameter types in Java are `java.lang.Integer` and `java.lang.String` respectively.

In short, the resulting Java method in the DAO will take the form:

```java
  public List<ActiveAccount> findClientsActiveAccounts(Integer clientId, String type) { ... }
```

Notice that:

- The method name is defined in the `<select>` tag.
- The return type is by default a `List<>` of the type specified in the `<select>` tag. It can also be a POJO or a cursor.
- The method includes both parameters.
- Although not shown in this example, each parameter can be used multiple times inside the query.


## Using Parameters with JEXL Syntax

Each parameter can be used in the query definition zero, one, or more times.

A parameter can be applied as is, directly to the query using the simple form `#{minBalance}`.

If the parameters is a complex object, maybe an array, a list, an object with properties, or
any combination of the above, then a JEXL expression can be used to specify the exact value to
use from this object. A JEXL expression uses bean syntax to access data structures. This includes
accessing:

- Properties
- Array elements
- List elements
- Maps keys and values
- Any Java method available in the object, using the JEXL syntax

For example, if the Java parameter `branch` defined below:

```java
  public class Department {
    public Department(int minSalary) { this.minSalary = minSalary; }
    public int minSalary;
  }
  
  public class Branch {
    public Branch(int id, Department[] dept) { this.id = id; this.dept = dept; }
    public int id;
    public Department[] dept;
  }
  
  Department[] depts = {new Department(90), new Department(105), new Department(101)};
  Branch branch = new Branch(1001, depts);
```

Can be passed to the query:

```xml
  <query method="findClientsActiveAccounts" vo="ActiveAccount">
    <parameter name="salaryIncrease" java-type="Integer" />
    <parameter name="branch" java-type="com.app.Branch" />
    UPDATE employee
    SET salary = salary + #{salaryIncrease}
    WHERE salary > #{branch.dept[1].minSalary}
  </query>
```
    
Even though the parameter is just `branch`, the query can fully accesses the value of:

```
  branch.dept[1].minSalary
```

This expression traverses the object, gets the second element of the array property, and then the property `minSalary` of this element.


## Applying Parameters vs Injecting Parameters

Applying parameters is the safe way of using a parameter in a SQL query and is implemented using the `#{expression}` syntax. 
This is the recommended way of using parameters that is known as *prepared statements* in many programming languages.

Injecting parameters &ndash; essentially concatenating parameters to the query as strings values &ndash; is an alternative way of
using parameters that may make your application vulnerable to SQL Injection. It's implemented using the `$INJECT{expression}` syntax.

> [!CAUTION]
> **IMPORTANT NOTE ON SECURITY**: SQL Injection opens the door for a security risk. There's a big difference between safely **applying** a parameter using `#{expression}` and directly **injecting** a String section into the query to be run using `$INJECT{expression}`. Only inject fully controlled Strings that are coming from inside the application, and never from any external source, such as a web parameter, an API, or a configuration file.


Nevertheless, depending on the specifics of a query, sometimes it's not possible to apply parameters but only to inject them as strings.
Parameter injection should be avoided if possible; if unavoidable, it needs to be used with extreme care.


## Location of Applied and Injected Expressions

**Applied parameters** are fully compliant JDBC parameters. They can typically placed in any place where a
normal *scalar* value would be allowed in the query. This includes:

 - In the `SELECT` clause
 - In the `WHERE` clause
 - In subqueries
 - In any other place the specific JDBC driver and the database engine allows it.
 
**Injected parameters**, on the other hand, are directly concatenated into the query and are not treated
as JDBC parameters. Therefore, they can be placed anywhere in the query.


## Rendering Parameters in SELECT queries

During column discovery the generator replaces each parameter with a [sample SQL value](#the-sample-sql-value)
of the corresponding type. For applied parameters this sample value depends on the corresponding JDBC type of
the parameter and the specific database engine; for injected parameters this is a dummy String value.

## The JDBC Type

In addition to its Java type HotRod may need to determine its JDBC type; usually the JDBC type of a parameter is inferred from the `java-type` 
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

When HotRod is in the process of discovering the resulting columns of a SELECT query it will replace each parameter
with a sample value. This value is auto-generated according to the `jdbc-type` as discussed before or it can be
directly specified using the `sample-sql-value` attribute in the &lt;parameter> tag, as in:

    <parameter name="clientId" java-type="Integer" sample-sql-value="123" />

The value in the `sample-sql-value` attribute is a SQL text value, not a Java value. It will replace the parameter in
the SELECT query with the aim of producing a valid SQL query. It may even be evaluated by the database engine used
during the code generation. The value can be as simple as `"123"` or maybe a more complex value such as
`"convert(timestamp, '2001-01-01 12:34:56')"`. In any case, it needs to evaluate to a valid database data type
during the code generation.

Although this attribute is rarely used, it can come in handy in the case the developer needs to use a parameter in the `SELECT` clause that needs to be correctly
typed when assembling the corresponding model class for the query, in the presence of uncommon or exotic parameter types if these types are not covered in the table below.

Finally, note these values serve for analysis purposes of the SQL statement during code generation only; they
are not present in the generated code, and are never used when running the application.

If not specified, the generator assigns a default SQL Sample Value, according to the following table:

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
     
