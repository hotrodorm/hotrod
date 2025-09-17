# Retrieving Column Properties

This guide retrieves the column properties of al the columns of a table, a view, and a nitro select. It's run using Maven and uses an H2 in-memory database.

You'll need:
- Java.
- Maven.
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer.
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example.

This guide sets up the Maven project, creates a table in the database, generates the HotRod persistence from it, and then runs a simple application using it.

After following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
pom.xml                    # The Maven project file
schema.sql                 # A SQL script that creates a table and data for this example
src/main/java              # Your Java app and the generated DAOs and VOs
src/main/resources         # All resources, including the generated mappers
application.properties     # The runtime properties
```

## Column Type Resolution

The columns of tables, views, and nitro selects are mapped into the application code according
to some custom and default rules. These rules are evaluated using the table and view meta data &mdash;
that is in *static* manned &mdash; or using live result set meta data &mdash; that is in a *runtime* manner.

The static resolution is evaluated while the persistence layer is being generated and set in stone in that opportunity. The dynamic resolution takes place when the query is executed at runtime.

**Note**: Since dynamic resolution uses the live meta data, this can result in column types being evaluated differently, for example, if the production database differs from the database in lower environments where the application was initially tested.


## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Set Up a Maven Project

If you are using a plain text editor (such as Notepad) you can create an empty folder and add the files as described in the
steps below. Alternatively, you can use your favorite IDE to create a blank Maven project.

The `pom.xml` will include:
- The Spring Boot Starter dependency and the Spring Boot Plugin.
- The JDBC driver dependency according to your specific database.
- The HotRod, HotRod LiveSQL, and MyBatis Libraries.
- The HotRod Generator Plugin.

The complete `pom.xml` file will look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>extract-columns-properties</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
  </properties>

  <dependencies />

  <build>

    <plugins>

      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>5.1.3</version>
        <configuration>
          <config-file>./layer.xml</config-file>
          <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:h2:mem:EX;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1</jdbcurl>
          <jdbcusername>sa</jdbcusername>
          <jdbcpassword>""</jdbcpassword>
          <jdbcschema>PUBLIC</jdbcschema>
          <txtexportfile>./export-columns.txt</txtexportfile> <!-- defines the TXT export file -->
        </configuration>
        <dependencies>
          <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.3.232</version>
          </dependency>
        </dependencies>
      </plugin>

    </plugins>

  </build>

</project>
```

Also, create the empty source folders, if they are not yet created. In linux you can do:

```bash
mkdir -p src/main/java
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; The Database Script

In this part we create an in-memory table in H2 database with a table and a view, using a SQL script. We then declare them in the configuration, along with a custom nitro SELECT query.

Create the file `schema.sql` with the following SQL content:

```sql
CREATE TABLE vehicle (
  id int PRIMARY KEY NOT NULL,
  brand varchar(15),
  country varchar(20)
);

INSERT INTO vehicle (id, brand, country) VALUES
  (1, 'Toyota', 'Japan'),
  (2, 'Buick', 'United States'),
  (3, 'Bentley', 'UK'),
  (4, 'Fiat', 'Italy'),
  (5, 'Nissan', 'Japan'),
  (6, 'Volkswagen', 'Germany');

CREATE VIEW japanese_vehicle AS
SELECT id, brand
FROM vehicle
WHERE country = 'Japan';
```

Create the file `layer.xml` as:

```xml
<hotrod>

  <generators/>
    <jdbc />
  </generators>

  <table name="vehicle" />

  <view name="japanese_vehicle" />

  <dao name="QueriesDAO">
    <select method="findNonJapaneseVehicles" vo="NJVehicle">
    SELECT v.id as vehicle_id, v.country
    FROM vehicle v
    LEFT JOIN japanese_vehicle j ON j.id = v.id
    WHERE j.id IS NULL
    </select>
  </dao>

</hotrod>
```

## Part 3 &mdash; Retrieving the Columns Properties

In this part we run the "Export Columns" HotRod functionality using Maven.

Go to the home dir of the project (where the pom.xml file is) and type:

```bash
mvn hotrod:export-columns-txt
```

The columns are exported to the TXT file `export-columns.txt`. This file looks like:

```
HotRod Column Export
--------------------

  From live database at : jdbc:h2:mem:EX;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
  Catalog               :
  Schema                : PUBLIC
  Exported              : 16 Sep 2025 at 20:53:19 -0400
  Generated by          : HotRod version 5.1.3

catalog  schema  objectName        ordinal  name     typeName           dataType  size  scale  default  autogeneration  belongsToPK  isVersionControlColumn  nature  nullable  native.TABLE_NAME  native.COLUMN_NAME  native.IS_IDENTITY  native.REMARKS  native.DEFAULT_ON_NULL  native.NUMERIC_PRECISION  native.NUMERIC_PRECISION_RADIX  native.GEOMETRY_TYPE  native.GEOMETRY_SRID  native.IDENTITY_MAXIMUM  native.COLLATION_NAME  native.GENERATION_EXPRESSION  native.COLUMN_ON_UPDATE  native.INTERVAL_PRECISION  native.IDENTITY_CYCLE  native.COLLATION_CATALOG  native.IS_GENERATED  native.CHARACTER_MAXIMUM_LENGTH  native.MAXIMUM_CARDINALITY  native.IDENTITY_INCREMENT  native.DECLARED_DATA_TYPE  native.DECLARED_NUMERIC_PRECISION  native.DATA_TYPE   native.CHARACTER_SET_NAME  native.INTERVAL_TYPE  native.CHARACTER_SET_SCHEMA  native.DOMAIN_CATALOG  native.TABLE_CATALOG  native.IS_NULLABLE  native.COLLATION_SCHEMA  native.TABLE_SCHEMA  native.DECLARED_NUMERIC_SCALE  native.CHARACTER_SET_CATALOG  native.CHARACTER_OCTET_LENGTH  native.IDENTITY_MINIMUM  native.DOMAIN_NAME  native.DOMAIN_SCHEMA  native.DATETIME_PRECISION  native.IDENTITY_CACHE  native.IDENTITY_BASE  native.IS_VISIBLE  native.NUMERIC_SCALE  native.IDENTITY_GENERATION  native.ORDINAL_POSITION  native.IDENTITY_START  native.DTD_IDENTIFIER  native.SELECTIVITY  native.COLUMN_DEFAULT
-------  ------  ----------------  -------  -------  -----------------  --------  ----  -----  -------  --------------  -----------  ----------------------  ------  --------  -----------------  ------------------  ------------------  --------------  ----------------------  ------------------------  ------------------------------  --------------------  --------------------  -----------------------  ---------------------  ----------------------------  -----------------------  -------------------------  ---------------------  ------------------------  -------------------  -------------------------------  --------------------------  -------------------------  -------------------------  ---------------------------------  -----------------  -------------------------  --------------------  ---------------------------  ---------------------  --------------------  ------------------  -----------------------  -------------------  -----------------------------  ----------------------------  -----------------------------  -----------------------  ------------------  --------------------  -------------------------  ---------------------  --------------------  -----------------  --------------------  --------------------------  -----------------------  ---------------------  ---------------------  ------------------  ---------------------
                 JAPANESE_VEHICLE        1  ID       INTEGER            4           32      0                           false                                        true      JAPANESE_VEHICLE   ID                  NO                                  false                   32                        2                                                     0                     0                                                                                                      0                                                                           NEVER                0                                0                           0                          INTEGER                    0                                  INTEGER                                                                                                                 EX                    YES                                          PUBLIC               0                                                            0                              0                                                                  0                          0                      0                     true               0                                                 1                        0                      1                      50                                       
                 JAPANESE_VEHICLE        2  BRAND    CHARACTER VARYING  12          15      0                           false                                        true      JAPANESE_VEHICLE   BRAND               NO                                  false                   0                         0                                                     0                     0                        OFF                                                                           0                                                 EX                        NEVER                15                               0                           0                                                     0                                  CHARACTER VARYING  Unicode                                          PUBLIC                                              EX                    YES                 PUBLIC                   PUBLIC               0                              EX                            15                             0                                                                  0                          0                      0                     true               0                                                 2                        0                      2                      50                                       
                 VEHICLE                 1  ID       INTEGER            4           32      0                           true                                         false     VEHICLE            ID                  NO                                  false                   32                        2                                                     0                     0                                                                                                      0                                                                           NEVER                0                                0                           0                          INTEGER                    0                                  INTEGER                                                                                                                 EX                    NO                                           PUBLIC               0                                                            0                              0                                                                  0                          0                      0                     true               0                                                 1                        0                      1                      50                                       
                 VEHICLE                 2  BRAND    CHARACTER VARYING  12          15      0                           false                                        true      VEHICLE            BRAND               NO                                  false                   0                         0                                                     0                     0                        OFF                                                                           0                                                 EX                        NEVER                15                               0                           0                                                     0                                  CHARACTER VARYING  Unicode                                          PUBLIC                                              EX                    YES                 PUBLIC                   PUBLIC               0                              EX                            15                             0                                                                  0                          0                      0                     true               0                                                 2                        0                      2                      50                                       
                 VEHICLE                 3  COUNTRY  CHARACTER VARYING  12          20      0                           false                                        true      VEHICLE            COUNTRY             NO                                  false                   0                         0                                                     0                     0                        OFF                                                                           0                                                 EX                        NEVER                20                               0                           0                                                     0                                  CHARACTER VARYING  Unicode                                          PUBLIC                                              EX                    YES                 PUBLIC                   PUBLIC               0                              EX                            20                             0                                                                  0                          0                      0                     true               0                                                 3                        0                      3                      50                                       
```

This file includes the properties of the columns of the table and view. It does include the standard JDBC meta data and also native properties provided by the database itself (prepended by `native.`).

These properties are useful to craft rules in the Type Solver.

