# Column Type Resolution

This example demonstrates the way that HotRod determines the static type of a column of a table, view, or Nitro select.

It also demonstrates the runtime type resolution for LiveSQL queries for table columns, free SQL expressions, and subcolumns derived from subqueries and CTEs.

The type resolution logic in all cases is identical for plain types and for converters.

## Type Resolution Precedence

The type resolution happens in two opportunities. It happens during the persistence layer generation when HotRod inspects the database tables, views, and Nitro quieries result sets. The types resolved by it are considered *static* types and they don't change at runtime.

It also happens for SQL expressions in LiveSQL queries. These are free expressions that can combine any number of operators, functions, parameters, and table columns. The types resolved in this manner are considered *runtime* types. Runtime types only apply to free SQL expressions and cannot override static types for table and view columns.


### A. Static Type Resolution

The types for columns of tables, views, and Nitro select queries are resolved during the generation of the persistence layer, and thus, are static. They are set in stone in the persistence layer and don't change at runtime.

The types are set by the following rules, in order. For each column the first rule that applies wins and the rest are ignored.

| Priority | Rule | Shown in Preview and Logs as | Can Use Converter |
| :--: | :-- | :-- | :--: |
| 1  | The &lt;column> Tag | STATIC_DESIGNATED | Yes |
| 2  | The &lt;type-solver> Static Rules | STATIC_TYPESOLVER_RULE | Yes |
| 3  | The Dialect Default Type | STATIC_DIALECT_RULE | No |

LiveSQL queries use the static type resolution for table and view columns in the result set. This means that in the result set of a LiveSQL query the types of the columns coming from a table are always the same as the ones used by CRUD. This includes all the static resolution rules for plain types and converters.

### B. Runtime Type Resolution

LiveSQL free SQL expressions in the result set &ndash; that is, any combination of operators, functions, parameters, or table columns &ndash; use the runtime type resolution.

The Runtime Type Resolution uses the following rules, in order. For each column the first rule that applies wins and the rest are ignored.

| Priority | Rule | Shown in Preview and Logs as |Can Use Converter |
| :--: | :-- | :-- | :--: |
| 1  | The `.type(<class>)` clause applied to the expression | RUNTIME_DESIGNATED | Yes |
| 2  | The &lt;type-solver> Runtime Rules | RUNTIME_TYPESOLVER_RULE | Yes |
| 3  | The Dialect Default Type | RUNTIME_DIALECT_RULE | No |
| 4  | The JDBC Driver Default Type | RUNTIME_JDBC_DRIVER_DEFAULT | No |

The Runtime Layer Rules are kept in the generated persistence layer bean `LayerConfigurationBean.java` class. This class is updated every time the persistence layer is re-generated to keep the rules up to date with the latest changes.



## Part 1 &mdash; Setting Up the Project

You'll need:

- Java installed
- Maven installed
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example

After following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
<PROJECT_HOME>
+- application.properties
+- pom.xml
+- layer.xml
+- schema.sql
+- src/main/java/app/persistence/
   +- LayerConfigurationBean.java
   +- dao/
      +- AccountDAO.java
   +- layout/
      +- AccountLayout.java
   +- model/
      +- Account.java
```

Create the `pom.xml` file as:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>typesolver</artifactId>
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
      <version>5.1.3</version>
    </dependency>

    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>2.1.214</version>
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
        <version>5.1.3</version>
        <configuration>
          <configfile>./layer.xml</configfile>
          <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1</jdbcurl>
          <jdbcusername>sa</jdbcusername>
          <jdbcpassword>""</jdbcpassword>
          <jdbcschema>PUBLIC</jdbcschema>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.1.214</version>
          </dependency>
        </dependencies>
      </plugin>

    </plugins>

  </build>

</project>
```

Also, create the empty source folders, if they are not yet created. In linux you can do:

```bash
mkdir -p src/main/java/app
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Defining the Queries

In this part we define the tables that will reside in the H2 database. We also define the custom Nitro queries that we want to run in it.

### Prepare the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
CREATE TABLE invoice (
  id INT PRIMARY KEY NOT NULL,
  client VARCHAR(50),
  created TIMESTAMP,
  amount DECIMAL(12, 2),
  tax_codes VARCHAR ARRAY[8],
  paid char(1) CHECK (paid IN ('Y', 'N'))
);

INSERT INTO invoice (id, client, created, amount, tax_codes, paid) VALUES
  (10, 'Los Alamos', '2022-04-10', 105.60, ARRAY ['1001'], 'Y'),
  (11, 'Blue Store', '2023-12-07', 230.05, ARRAY ['2077', '2078', '2301'], 'N'),
  (12, 'Ultima Games', '2024-09-12', 140.49, ARRAY ['1001', '1002'], 'Y'),
  (13, 'Daily Bagel', '2025-05-28', 49.99, null, 'N');
```

### Define the Converters, Tables, and Nitro Queries

Create the `layer.xml` file with:

```xml
<hotrod>

  <generators>
    <jdbc/>
  </generators>

  <converter name="YNConverter"
    java-raw-type="String" java-type="Boolean" class="app.YNBooleanConverter" />

  <converter name="TaxCodesArrayConverter"
    java-raw-type="java.sql.Array" java-type="String[]" class="app.TaxCodesArrayConverter" />

  <table name="invoice">
    <column name="amount" java-type="Double" />
    <column name="paid" converter="YNConverter" />
    <column name="tax_codes" converter="TaxCodesArrayConverter" />
  </table>

  <dao name="PaymentDAO">

    <select method="getBigInvoices" vo="BigInvoice">
      <column name="amount" java-type="Double" />
      <column name="paid" converter="YNConverter" />
      <column name="tax_codes" converter="TaxCodesArrayConverter" />
      SELECT *
      FROM invoice
      WHERE amount > 1.2 * (SELECT avg(amount) FROM invoice)
    </select>

  </dao>

</hotrod>
```

### Generate the Persistence Layer

Now, let's generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

The layer generation reports:

```bash
[INFO] HotRod Generator version 5.1.3 (build 20250922-193506) - Generate
[INFO] 
[INFO] Configuration File: ./layer.xml
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.1 (2.1.214 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.1 (2.1.214 (2022-06-13)) - implements JDBC Specification 4.2
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
[INFO] Total of: 1 table, 0 views, 0 enums, 1 DAO, and 0 sequences -- including 1 select method, and 0 query methods.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

HotRod connected to the database schema, discovered the details of the table and queries, and generated the persistence layer.

All generated classes are inside `src/main/java/app/persistence`.


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application
class `src/main/java/app/App.java` as:

```java
package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.InvoiceDAO;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.Invoice;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoTypeSolver1();
    };
  }

  private void demoTypeSolver1() {
    Invoice inv = this.invoiceDAO.select(11);
    System.out.println("Invoice #10: " + inv);
  }

}
```

And the converter class `src/main/java/app/YNBooleanConverter.java` as:

```java
package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;

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

And the converter class `src/main/java/app/TaxCodesArrayConverter.java` as:

```java
package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.converter.TypeConverter;

public class TaxCodesArrayConverter implements TypeConverter<java.sql.Array, String[]> {

  @Override
  public String[] decode(java.sql.Array raw, Connection conn) {
    if (raw == null)
      return null;
    try {
      Object[] o = (Object[]) raw.getArray();
      String[] value = Arrays.stream(o).toArray(String[]::new);
      return value;
    } catch (SQLException e) {
      throw new RuntimeException("Could not convert a database VARCHAR ARRAY into a String[].", e);
    }
  }

  @Override
  public java.sql.Array encode(String[] value, Connection conn) {
    if (value == null)
      return null;
    try {
      return conn.createArrayOf("CHARACTER VARYING ARRAY", value);
    } catch (SQLException e) {
      throw new RuntimeException("Could not convert String[" + value.length + "] to a VARCHAR ARRAY. The values are: "
          + Arrays.stream(value).collect(Collectors.joining(", ")));
    }
  }

}
```

### The Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
```


### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs both queries. We see the result shown below:

```log
```

Later on, if the database suffers changes that affect your Nitro queries you can just
rerun the generation step `mvn hotrod:gen` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
