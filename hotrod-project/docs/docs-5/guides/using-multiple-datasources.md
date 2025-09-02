# Using Multiple Datasources

This guide shows how to configure two datasources, each one having its own persistence layer. The datasource are not limited to use the same database brand, edition, or version; in fact, they can use different brands of databases and dialects.

This strategy can be used to use any number of static datasources, that is, data sources known at compilation time. Each datasource is configured independently and can be
defined for the database of the same or different brand.

Using Spring beans it's also possible to register and use a dynamic list of datasources. This can
be useful in special cases where the number of databases vary at runtime. The dynamic creation,
registration, and use of dynamic beans is out of the scope of this guide.

For a quick overview on how to set up a simple project with a single datasource only, see
[Hello World](./hello-world.md).

This guide uses two separate H2 databases, each one having a different table.


## Part 1 &mdash; Setting Up the Project

First we create the Maven project, we lay out its structure, we and add all the necessary libraries
and plugins to it.

### Setting Up the Maven Project

The Maven project will define two profiles, one for each database's code generation, that are inactive by default. This example uses two H2 databases (with different tables) but it can accommodate any number of databases of the same or different brand.

**Note**: For compatibility purposes, this example uses the basic Java 8 and Spring Boot 2.x engine. Consider using a more modern Java version and any modern with Spring Boot as well.


The `pom.xml` file will look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>twods</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <hotrod.version>5.1.2</hotrod.version>
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
      <version>${hotrod.version}</version>
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

    </plugins>

  </build>

  <profiles>

    <profile>
      <id>first</id> <!-- Only the table "account" exists here -->
      <build>
        <plugins>
          <plugin>
            <groupId>org.hotrodorm.hotrod</groupId>
            <artifactId>hotrod-maven-plugin</artifactId>
            <version>${hotrod.version}</version>
            <configuration>
              <configfile>./first.xml</configfile>
              <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
              <jdbcurl>jdbc:h2:mem:FIRSTDB;INIT=runscript from './database1.sql';DB_CLOSE_DELAY=-1</jdbcurl>
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
    </profile>

    <profile>
      <id>second</id> <!-- Only the table "invoice" exists here -->
      <build>
        <plugins>
          <plugin>
            <groupId>org.hotrodorm.hotrod</groupId>
            <artifactId>hotrod-maven-plugin</artifactId>
            <version>${hotrod.version}</version>
            <configuration>
              <configfile>./second.xml</configfile>
              <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
              <jdbcurl>jdbc:h2:mem:SECONDDB;INIT=runscript from './database2.sql';DB_CLOSE_DELAY=-1</jdbcurl>
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
    </profile>

  </profiles>

</project>
```

Create the empty source folders, if they are not yet created. In linux you can do (change accordingly for other OSes):

```bash
mkdir -p src/main/java/app
mkdir -p src/main/resources
```

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating the Schemas and Generating Both Persistence Layers

In this part we create two in-memory schemas in H2 and we generate both persistence layers from them.


### Preparing the Database Scripts

For the first database create the file `database1.sql` with the following SQL content:

```sql
drop table if exists account;

create table account (
  id int primary key not null,
  owner varchar(50),
  balance int
);

insert into account (id, owner, balance) values (1, 'Tom', 500);
insert into account (id, owner, balance) values (2, 'Kim', 450);
insert into account (id, owner, balance) values (3, 'Sue', 830);
```

Then, for the second database create the file `database2.sql` with the following SQL content:

```sql
drop table if exists invoice;

create table invoice (
  id int primary key not null,
  client varchar(50),
  amount int
);

insert into invoice (id, client, amount) values (101, 'Acme', 78);
insert into invoice (id, client, amount) values (102, 'Indus', 1680);
insert into invoice (id, client, amount) values (103, 'Lotus Inc', 450);
```


### Preparing the HotRod Configuration Files

Tell HotRod how you want the generation to work for each database. Create the file
`first.xml` for the first database:

```xml
<hotrod>

  <generators>
    <jdbc package="app.persistence1" qualifier="accounting" />
  </generators>

  <table name="account" />

</hotrod>
```

Then, create the file `second.xml` for the second database:

```xml
<hotrod>

  <generators>
    <jdbc package="app.persistence2" qualifier="sales" />
  </generators>

  <table name="invoice" />

</hotrod>
```

Notice that the `qualifier` property is different in the persistence layers. That allows each persistence layer to use the correct datasource, LiveSQL engine, and SQL dialects.


### Generating the Persistence Layers

Now, let's use HotRod to generate the persistence layers.

Generate the persistence layer for the first database. Type:

```bash
mvn -P first hotrod:gen
```

The generated persistence layer includes all the beans necessary to use this datasource.

Generate the persistence layer for the second database. Type:

```bash
mvn -P second hotrod:gen
```

Again, the second generated persistence layer includes all the beans necessary to use it.

Each time HotRod connected to the corresponding database schema, retrieved the table details, and produced the persistence layer.


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities from each database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in each database. Create the application
class `src/main/java/app/App.java` as:

```java
package app;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence1.dao.AccountDAO;
import app.persistence1.dao.AccountDAO.AccountTable;
import app.persistence1.model.Account;
import app.persistence2.dao.InvoiceDAO;
import app.persistence2.dao.InvoiceDAO.InvoiceTable;
import app.persistence2.model.Invoice;

@Configuration
@SpringBootApplication
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  @Qualifier("liveSQL:accounting") // bean name defined in DataSourceConfig1.java
  private LiveSQL sql1;

  @Autowired
  @Qualifier("liveSQL:sales") // bean name defined in DataSourceConfig2.java
  private LiveSQL sql2;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() throws DynamicExpressionException, SQLException {

    System.out.println();

    // Use a DAO to search in datasouce #1
    {
      Account a = this.accountDAO.select(2);
      System.out.println("1. Account #2: " + a);
      System.out.println();
    }

    // Use a DAO to search in datasouce #2
    {
      Invoice i = this.invoiceDAO.select(103);
      System.out.println("2. Invoice #103: " + i);
      System.out.println();
    }

    // Use LiveSQL to search in datasource #1
    {
      AccountTable a = this.accountDAO.newTable();
      List<Row> rows = this.sql1.select().from(a).where(a.owner.like("%m%")).execute();
      System.out.println("3. Account which owner names' have an 'm':");
      for (Row r : rows) {
        System.out.println(r);
      }
      System.out.println();
    }

    // Use LiveSQL to search in datasource #2
    {
      InvoiceTable i = this.invoiceDAO.newTable();
      List<Row> rows = this.sql2.select().from(i).where(i.amount.ge(300)).execute();
      System.out.println("4. Invoices for more than $300:");
      for (Row r : rows) {
        System.out.println(r);
      }
      System.out.println();
    }

  }

}
```

### Prepare the Runtime Properties File

The runtime properties provide details of each database for the running the application.

In this example we:

- Disable the auto-instantiation of the default Spring dataSource.
- Disable the auto-instantiation of the default HotRod beans for single data sources.
- Configure both data sources.

Create the file `application.properties` as:

```properties
# General configuration of the app

logging.level.root=INFO

spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
org.hotrod.livesql.autoconfig.LiveSQLAutoConfiguration

# First datasource configuration

datasource.accounting.driver-class-name=org.h2.Driver
datasource.accounting.url=jdbc:h2:mem:FIRSTDB;INIT=runscript from './database1.sql';DB_CLOSE_DELAY=-1
datasource.accounting.username=sa
datasource.accounting.password=

#datasource.accounting.livesqldialectname=MYSQL
#datasource.accounting.livesqldialectdatabaseName=MariaDB
#datasource.accounting.livesqldialectversionString="10.6"
#datasource.accounting.livesqldialectmajorVersion=10
#datasource.accounting.livesqldialectminorVersion=6

# Second datasouce configuration

datasource.sales.driver-class-name=org.h2.Driver
datasource.sales.url=jdbc:h2:mem:SECONDDB;INIT=runscript from './database2.sql';DB_CLOSE_DELAY=-1
datasource.sales.username=sa
datasource.sales.password=

#datasource.sales.livesqldialectname=POSTGRESQL
#datasource.sales.livesqldialectdatabaseName="PostgreSQL 12"
#datasource.sales.livesqldialectversionString="12.4 (Linux)"
#datasource.sales.livesqldialectmajorVersion=12
#datasource.sales.livesqldialectminorVersion=4
```

**Note**: The properties commented out can be used to change the database dialect at runtime.

### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and run both queries. We see the result shown below:

```log
[ Starting example ]

1. Account #2: app.persistence1.model.Account@dab1f89
- id=2
- owner=Kim
- balance=450

2. Invoice #103: app.persistence2.model.Invoice@5e13f156
- id=103
- client=Lotus Inc
- amount=450

3. Account which owner names' have an 'm':
{owner=Tom, balance=500, id=1}
{owner=Kim, balance=450, id=2}

4. Invoices for more than $300:
{amount=1680, client=Indus, id=102}
{amount=450, client=Lotus Inc, id=103}

[ Example complete ]
```

That's it! You just built and run a Spring Boot app with two datasources, each one with
its own persistence layer.

