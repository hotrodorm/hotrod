# Hello Type Solver

This guide runs a Spring Boot project with Maven and an H2 in-memory database. It shows the details on how the data types for the SELECT query columns are decided for your app.

It demonstrates all 11 type solver cases described in [Type Solver Cases](./type-solver-cases.md).

You'll need:

- Java
- Maven
- A text editor. `Notepad` or `vi` will do, or you can use your favorite IDE if you prefer
- No database installation necessary. We'll use an in-memory H2 database in this example

After following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
application.properties     # The runtime properties of your app
layer.xml                  # The configuration details of your persistence layer
pom.xml                    # The Maven project file
schema.sql                 # A SQL script that creates a table and data for this example
src/main/java              # Your app, your converters, and the generated persistence layer
```


## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Set Up a Maven Project

If you are using a plain text editor (such as Notepad) you can create an empty folder and add
the files as described in the steps below. Alternatively, you can use your favorite IDE to create
a blank Maven project.

Add the `pom.xml` file:

```xml
<project
  xmlns=
    "http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation=
    "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hello-typesolver</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
  </properties>

  <dependencies>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>5.1.4</version>
    </dependency>

    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>2.2.224</version>
    </dependency>

  </dependencies>

  <build>

    <plugins>

      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>2.3.4.RELEASE</version>
        <executions>
          <execution>
            <goals>
              <goal>repackage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>5.1.4</version>
        <configuration>
          <configfile>./layer.xml</configfile>
          <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:h2:mem:DB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1</jdbcurl>
          <jdbcusername>sa</jdbcusername>
          <jdbcpassword>""</jdbcpassword>
          <jdbcschema>PUBLIC</jdbcschema>
          <txtexportfile>./export-columns.txt</txtexportfile>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version>
          </dependency>
        </dependencies>
      </plugin>

    </plugins>

  </build>

</project>
```

Also, create the empty source folder. In linux you can do:

```bash
mkdir -p src/main/java/app
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create it.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Generating the Persistence Layer

In this part we create an in-memory table in an H2 database and we generate the persistence layer from it.


### Preparing the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
CREATE TABLE invoice (
  amount DECIMAL(12, 2),
  status INT,
  created TIMESTAMP,
  active CHAR(1) CHECK (active IN ('Y', 'N') ),
  category DECIMAL(4)
);

INSERT INTO invoice (amount, status, created, active, category) VALUES
  (110.05, 3, '2022-09-14 12:30:21', 'Y', 1015),
  (51.99,  2, '2023-07-22 14:31:35', 'N', 2001),
  (480.14, 1, '2024-02-03 07:32:23', 'Y', 1018),
  (224.01, 3, '2025-10-18 20:33:04', 'N', 1023);
```

### Configuring Persistence Layer

Create the file `layer.xml` with the following content:

```xml
<hotrod>

  <type-solver>
    <when test="name == 'CREATED'" java-type="java.time.LocalDateTime" />
    <when test="name == 'ACTIVE'" converter="YNConverter" />
  </type-solver>

  <runtime-type-solver>
    <when test="columnName == 'dom'" java-type="Long" />
    <when test="columnName.endsWith('Branch')" converter="YNConverter" />
  </runtime-type-solver>

  <converter name="YNConverter" java-raw-type="java.lang.String" java-type="java.lang.Boolean"
    class="app.YNBooleanConverter" />
  <converter name="InvoiceStatusConverter" java-raw-type="java.lang.Integer"
    java-type="app.InvoiceStatus" class="app.InvoiceStatusConverter" />

  <table name="invoice">
    <column name="amount" java-type="Double" />
    <column name="status" converter="InvoiceStatusConverter" />
  </table>

  <dao name="PaymentDAO">

    <select method="getBigInvoices" vo="BigInvoice">
      <column name="amount" java-type="Double" />
      <column name="status" converter="InvoiceStatusConverter" />
      SELECT *
      FROM invoice
      WHERE amount > 200
    </select>

  </dao>

</hotrod>
```

### Generating the Persistence Layer

Now, let's use generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

We see the code generation details:

```bash
[INFO] HotRod Generator version 5.1.4 (build 20251011-230958) - Generate
[INFO] 
[INFO] Configuration File: ./layer.xml
[INFO] Database URL: jdbc:h2:mem:DB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.2 (2.2.224 (2023-09-17))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.2 (2.2.224 (2023-09-17)) - implements JDBC
       Specification 4.2
[INFO] HotRod Adapter: H2 Adapter
[INFO]  
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO] Discover disabled.
[INFO]  
[INFO] Generating all facets.
[INFO]  
[INFO] Table INVOICE included.
[INFO] DAO PaymentDAO included.
[INFO]  - Select getBigInvoices included.
[INFO]  
[INFO] Total of: 1 table, 0 views, 0 enums, 1 DAO, and 0 sequences -- including 1 select method,
       and 0 query methods.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

HotRod connected to the database schema, discovered the tables in it, retrieved their details, and generated the persistence layer accordingly.


## Part 3 &mdash; The Application

In this part we write a simple app that shows all the type solver options.


### 1. The Main Spring Boot Application

Now we write a simple app that uses CRUD, Nitro, and LiveSQL to run SELECT queries in the database. Create the main application class `src/main/java/app/App.java` as:

```java
package app;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.InvoiceDAO;
import app.persistence.dao.InvoiceDAO.InvoiceTable;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.BigInvoice;
import app.persistence.model.Invoice;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger LOG = Logger.getLogger(App.class.getName());

  private static final LiveSQLLogging LIVESQL_LOG =
    LiveSQLLogging.of(() -> LOG.isLoggable(Level.FINE), msg -> LOG.fine(msg));

  @Autowired
  private LiveSQL sql;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  @Autowired
  private H2Functions h2;

  @Autowired
  private YNBooleanConverter ynBooleanConverter;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoTypeSolverCRUD();
      demoTypeSolverNitroSelect();
      demoTypeSolverLiveSQL();
    };
  }

  private void demoTypeSolverCRUD() {
    Invoice filter = new Invoice();
    filter.setStatus(InvoiceStatus.UNPAID);
    List<Invoice> unpaid = this.invoiceDAO.select(filter);
    System.out.println("== Returns ==");
    for (Invoice inv : unpaid) {
      System.out.println("1. Unpaid invoice: " + inv);
    }
  }

  private void demoTypeSolverNitroSelect() {
    List<BigInvoice> bi = this.paymentDAO.getBigInvoices();
    System.out.println("== Returns ==");
    for (BigInvoice i : bi) {
      System.out.println("2. Big Invoice: " + i);
    }
  }

  private void demoTypeSolverLiveSQL() {
    InvoiceTable i = this.invoiceDAO.newTable();
    List<Row> rows = this.sql
        .select(
          i.amount,
          i.status,
          i.created,
          i.active,
          i.category,
          i.amount.mult(1.30).as("gross").type(Double.class),
          sql.caseWhen(i.amount.ge(300), "Y").elseValue("N").end()
            .as("vip").type(this.ynBooleanConverter),
          i.created.extract(DateTimeField.DAY).as("dom"),
          sql.caseWhen(i.category.le(1999), "Y").elseValue("N").end().as("mainBranch"),
          sql.caseWhen(i.status.eq(InvoiceStatus.PAID), i.amount).elseValue(0).end()
            .as("paidAmount"),
          h2.randomUUID().as("globalId")
        )
        .from(i)
        .where(i.status.eq(InvoiceStatus.DRAFT))
        .execute(LIVESQL_LOG);
   System.out.println("== Returns ==");
   for (Row r : rows) {
      System.out.println("3. row=" + r);
    }
  }

}
```

### 2. One Converter

The first converter needs a single class. Create the converter class `src/main/java/app/YNBooleanConverter.java` as:

```java
package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class YNBooleanConverter implements TypeConverter<String, Boolean> {

  private static String TRUE = "Y";
  private static String FALSE = "N";

  @Override
  public Boolean decode(String raw, Connection conn) {
    if (raw == null)
      return null;
    if (FALSE.equals(raw))
      return false;
    if (TRUE.equals(raw))
      return true;
    throw new RuntimeException("Could not convert raw-value '" + raw + "' to a Boolean value");
  }

  @Override
  public String encode(Boolean value, Connection conn) {
    if (value == null)
      return null;
    return value ? TRUE : FALSE;
  }

}
```

### 3. The Other Converter

For the second converter, create the domain enum `src/main/java/app/InvoiceStatus.java` as:

```java
package app;

public enum InvoiceStatus {

  DRAFT(1), UNPAID(2), PAID(3);

  private int code;

  private InvoiceStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
```

And then the corresponding converter class `src/main/java/app/InvoiceStatusConverter.java` as:

```java
package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceStatusConverter implements TypeConverter<Integer, InvoiceStatus> {

  @Override
  public InvoiceStatus decode(Integer raw, Connection conn) {
    if (raw == null)
      return null;
    for (InvoiceStatus domain : InvoiceStatus.values()) {
      if (domain.getCode() == raw) {
        return domain;
      }
    }
    throw new RuntimeException("Could not convert raw-value '" + raw + "' to a InvoiceStatus value");
  }

  @Override
  public Integer encode(InvoiceStatus domain, Connection conn) {
    if (domain == null)
      return null;
    return domain.getCode();
  }

}
```

### 4. A Custom LiveSQL Function

Finally, define an H2 custom function in the class `src/main/java/app/H2Functions.java`:

```java
package app;

import org.hotrod.livesql.expressions.Function;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.springframework.stereotype.Component;

@Component
public class H2Functions {

  public ObjectFunction randomUUID() {
    return Function.returnsObject("RANDOM_UUID()");
  }

}
```

This bean implements a single function to demonstrate the JDBC driver default class when using a non-trivial column type.

### 5. The Runtime Properties File

The runtime properties are used when running the application. Create the file `./application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:DB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

logging.level.app.App=DEBUG
logging.level.app.persistence.dao=DEBUG
```

Note that we enable logging to see the actual queries being run. We do this in the App itself for the LiveSQL SELECTs and, separately, in the DAOs for the CRUD and Nitro SELECTs.

## Running the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs the three demo examples. The first one shows:

#### 1. The CRUD Select

The log shows:

```log
SELECT
  amount,
  status,
  created,
  active,
  category
FROM invoice
WHERE status = ?
== Returns ==
1. Unpaid invoice: app.persistence.model.Invoice@478b0739
- amount=51.99
- status=UNPAID
- created=2023-07-22T14:31:35
- active=false
- category=2001
```

All CRUD SELECT queries retreive the table column using the types defined in the
layout class for the table. In this case, the class `app.persistence.model.Invoice`.
If we inspect this class we can see the columns as app properties with the types:

```java
  protected Double amount = null; // Type Source: STATIC_DESIGNATED
  protected InvoiceStatus status = null; // Type Source: STATIC_DESIGNATED
  protected LocalDateTime created = null; // Type Source: STATIC_TYPESOLVER_RULE, rule #T1
  protected Boolean active = null; // Type Source: STATIC_TYPESOLVER_RULE, rule #T2
  protected Short category = null; // Type Source: STATIC_DIALECT_RULE, rule #D3
```

All types are computed during the persistence layer generation and don't change at runtime;
therefore, they are all `STATIC`. In this case:

- The `amount` column's type was designated as `Double` using a `<column>` tag, that we can find in the `layer.xml` file in line 19.
- The `status` column's type was also designated, this time using the converter `InvoiceStatusConverter`. This is defined in
a `<column>` tag, that we can find in the `layer.xml` file in line 20.
- The `created` column's type was not designated. It was computed as `java.time.LocalDateTime` in the static type solver's rule #1 (aka #T1),
that we can find in the `layer.xml` file in line 4.
- The `active` column's type was computed by the static type solver for the `YNConverter`, using the second rule in it (aka #T2);
we can find this rule in the `layer.xml` file in line 5.
- The `category` column's type was not designated nor computed by the static type solver; it was decided by a dialect rule. In this case,
by rule #D3. The rules for the dialect are described in [H2's Static Dialect Rules]](../database-support/h2.md#1-static-dialect-rules).

#### 2. The Nitro Select

```log
SELECT *
FROM invoice
WHERE amount > 200
== Returns ==
2. Big Invoice: app.persistence.model.BigInvoice@3f63a513
- amount=480.14
- status=DRAFT
- created=2024-02-03T07:32:23
- active=true
- category=1018
2. Big Invoice: app.persistence.model.BigInvoice@413bef78
- amount=224.01
- status=PAID
- created=2025-10-18T20:33:04
- active=false
- category=1023
```

#### 3. The LiveSQL Select

```log
ELECT
  a.amount as "amount", 
  a.status as "status", 
  a.created as "created", 
  a.active as "active", 
  a.category as "category", 
  a.amount * ? as "gross", 
  case when (a.amount >= ?) then ? else ? end as "vip", 
  extract(day from a.created) as "dom", 
  case when (a.category <= ?) then ? else ? end as "mainBranch", 
  case when (a.status = ?) then a.amount else ? end as "paidAmount", 
  RANDOM_UUID() as "globalId"
FROM invoice a
WHERE a.status = ?
--- Parameters (10) -------
 * 1 (java.lang.Double): 1.3
 * 2 (java.lang.Integer): 300
 * 3 (java.lang.String, length=1): Y
 * 4 (java.lang.String, length=1): N
 * 5 (java.lang.Integer): 1999
 * 6 (java.lang.String, length=1): Y
 * 7 (java.lang.String, length=1): N
 * 8 (java.lang.Integer): 3
 * 9 (java.lang.Integer): 0
 * 10 (java.lang.Integer): 1
--- Query Columns (11) ---
 * 1 amount: java.lang.Double, source: STATIC_DESIGNATED
 * 2 status: app.InvoiceStatus (⚙InvoiceStatusConverter), source: STATIC_DESIGNATED
 * 3 created: java.time.LocalDateTime, source: STATIC_TYPESOLVER_RULE, rule #T1
 * 4 active: java.lang.Boolean (⚙YNBooleanConverter), source: STATIC_TYPESOLVER_RULE, rule #T2
 * 5 category: java.lang.Short, source: STATIC_DIALECT_RULE, rule #D3
 * 6 gross: java.lang.Double, source: RUNTIME_DESIGNATED
 * 7 vip: java.lang.Boolean (⚙YNBooleanConverter), source: RUNTIME_DESIGNATED
 * 8 dom: java.lang.Long, source: RUNTIME_TYPESOLVER_RULE, rule #RT1
 * 9 mainBranch: java.lang.Boolean (⚙YNBooleanConverter), source: RUNTIME_TYPESOLVER_RULE, rule #RT2
 * 10 paidAmount: java.math.BigDecimal, source: RUNTIME_DIALECT_RULE, rule #RD1
 * 11 globalId: java.util.UUID, source: RUNTIME_JDBC_DRIVER_DEFAULT
---------------------
== Returns ==
3. row={amount=480.14, dom=3, gross=624.182, created=2024-02-03T07:32:23,
globalId=1009b80f-a05a-4de9-9fec-6577aa5c0390, active=true, mainBranch=true,
category=1018, vip=true, paidAmount=0, status=DRAFT}
```

That's it! You just generated the persistence code from the database and ran an app using it.

Later on, when the database suffers changes &mdash; it will &mdash; you can just
rerun the generation step `mvn hotrod:gen` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
