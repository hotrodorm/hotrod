# Hello Nitro!

This example runs four Nitro queries in the database. It shows the basics on how to define and use them from your application, including the use of parameters and Dynamic SQL.

There are two types of Nitro queries:

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

## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

Create the `pom.xml` file as:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hellonitro</artifactId>
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
create table account (
  id int primary key not null,
  title varchar(20),
  created date,
  balance int
);

insert into account (id, title, created, balance) values
  (107, 'CHK1010', '2023-09-10', 100),
  (108, 'SAV2307', '2024-09-11', 20),
  (109, 'CHK1015', '2025-09-12', 140),
  (110, 'SAV2308', '2025-09-15', 45);
```

### Define the Nitro Queries

Create the `layer.xml` file with:

```xml
<hotrod>

  <generators>
    <jdbc/>
  </generators>

  <table name="account">

    <query method="applyMonthlyCharge">
      <parameter name="amount" java-type="Integer" />
      UPDATE account SET balance = balance - #{amount}
    </query>

    <select method="findSavingAccounts">
      <parameter name="year" java-type="Integer" />
      SELECT *
      FROM account
      WHERE title LIKE 'SAV%'
      <if test="year != null">
        AND year(created) = #{year}
      </if>
    </select> 

  </table>

  <dao name="ReportingDAO">

    <query method="deleteOldNegativeAccounts">
      <parameter name="maxYear" java-type="Integer" />
      DELETE FROM account WHERE year(created) &lt;= #{maxYear} AND balance &lt; 0
    </query>

    <select method="getTotals" vo="ReportingTotals" mode="single-row">
      <parameter name="minDate" java-type="java.time.LocalDate" />
      <parameter name="maxDate" java-type="java.time.LocalDate" />
      SELECT sum(balance) AS balance, count(*) AS count
      FROM account
      WHERE created BETWEEN #{minDate} AND #{maxDate}
    </select>

  </dao>

</hotrod>
```


These four Nitro queries become available to your application in the correspondig DAOs. Specifically at:

- AccountDAO:
    - `public int applyMonthlyCharge(Integer amount)`
    - `public List<Account> findSavingAccounts(Integer year)`
- ReportingDAO:
    - `public int deleteOldNegativeAccounts()`
    - `public ReportingTotals getTotals(LocalDate minDate, LocalDate maxDate)`

A few notes:

1. The `<query>` Nitro methods return an `int` that represent the number of rows affected by the query.
2. The `<select>` Nitro methods return SQL rows.
    - The `<select>` Nitro methods that belong to a table or view can only return rows of the type of the corresponding table or view.
    - The `<select>` Nitro methods that belong to a generic `<dao>` tag are free to return any type of row. As such the `vo` must be specified, to create new Layout and Model classes to represent the result set.
3. By default `<select>` Nitro methods return a `List<Model>`. When using `mode="cursor"` they return a `Cursor<Model>`. When using `mode="single-row"` Nitro understand that the query return one row at most, and the return type is simply `Model`.
4. Since the Nitro queries are specified in an XML file, they must abide with the XML syntax. That is, the `&` and `<` need to be escaped in XML as `&amp;` and `&lt;` as you can see in the method `deleteOldNegativeAccounts`. Other less common characters may need to be escaped too.
5. A Nitro query or select can have zero, one, or many parameters.
6. A Nitro query or select can use Dynamic SQL. In this example the method `findSavingAccounts` uses a simple `<if>` to conditionally include a SQL segment at runtime based on the parameter values. The query can use any number of Dynamic SQL segments, in flat or nested form.


### Generate the Persistence Layer

Now, let's generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

The layer generation reports:

```bash
[INFO] HotRod Generator version 5.1.3 (build 20250920-205252) - Generate
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
[INFO] Table ACCOUNT included.
[INFO]  - Query applyMonthlyCharge included.
[INFO]  - Select findSavingAccounts included.
[INFO] DAO ReportingDAO included.
[INFO]  - Query deleteOldNegativeAccounts included.
[INFO]  - Select getTotals included.
[INFO]  
[INFO] Total of: 1 table, 0 views, 0 enums, 1 DAO, and 0 sequences -- including 2 select methods, and 2 query methods.
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

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.AccountDAO;
import app.persistence.dao.ReportingDAO;
import app.persistence.model.Account;
import app.persistence.model.ReportingTotals;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private ReportingDAO reportingDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoNitro1();
      demoNitro2();
      demoNitro3();
      demoNitro4();
    };
  }

  private void demoNitro1() {
    int count = this.accountDAO.applyMonthlyCharge(15);
    System.out.println("Montly charge applied to " + count + " account(s).");
  }

  private void demoNitro2() {
    List<Account> accounts = this.accountDAO.findSavingAccounts(2024);
    for (Account a : accounts) {
      System.out.println("Saving account:" + a);
    }
  }

  private void demoNitro3() {
    int count = this.reportingDAO.deleteOldNegativeAccounts(2015);
    System.out.println("Deleted a total " + count + " old account(s).");
  }

  private void demoNitro4() {
    ReportingTotals totals = this.reportingDAO.getTotals(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 30));
    System.out.println("September 2025: " + totals.getCount() + " accounts, $" + totals.getBalance() + " balance.");
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
Montly charge applied to 4 account(s).
Saving account:app.persistence.model.Account@7a687d8d
- id=108
- title=SAV2307
- created=2024-09-11
- balance=5
Deleted a total 0 old account(s).
September 2025: 2 accounts, $155 balance.
```

Later on, if the database suffers changes that affect your Nitro queries you can just
rerun the generation step `mvn hotrod:gen` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
