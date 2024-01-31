# Hello Nitro!

This guide runs a Spring Boot project with Maven and the H2 in-memory database. It implements a Graph Query from the Nitro functionality.

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
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>demo</groupId>
  <artifactId>hello-nitro-graph</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
  </properties>

  <dependencies>

    <dependency> <!-- You can use Spring Boot, plain Spring, or other -->
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>

    <dependency> <!-- Required. The main HotRod library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod</artifactId>
      <version>4.5.3</version>
    </dependency>

    <dependency> <!-- Required. HotRod's LiveSQL library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>4.5.3</version>
    </dependency>

    <dependency> <!-- Required. The generator uses MyBatis for database connectivity -->
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>2.1.3</version>
    </dependency>

    <dependency> <!-- Your app needs the JDBC driver to connect to the database. Can be provided at runtime -->
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
        <version>4.5.3</version>
        <configuration>
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
mkdir -p src/main/resources
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Generating the Persistence Code

In this part we define the Graph query and we define the tables in the in-memory H2 database. With them we generate the persistence layer.


#### The HotRod Configuration File

Tell HotRod about the tables you have and the Graph query you want to define. Create the file `hotrod.xml` and add:

```xml
<hotrod>

  <generators>
    <mybatis-spring>
    </mybatis-spring>
  </generators>
  
  <table name="customer" />
  <table name="invoice" />
  <table name="invoice_line" />
  <table name="product" />
  <table name="category" />
 
  <dao name="InvoiceQueries">

    <select method="searchInvoices">
      select
        <columns>
          <vo table="invoice" extended-vo="InvoiceWithLines" alias="i">
            <association table="customer" property="customer" alias="c" />
            <collection table="invoice_line" extended-vo="LineWithProduct" property="lines" alias="l"> 
              <expression property="q2">l.qty * 2</expression>
              <association table="product" extended-vo="ProductWithCategory" property="product" alias="p">
                <association table="category" property="category" alias="t" />
              </association>
            </collection>
          </vo>
        </columns>
      from invoice i
      join customer c on c.id = i.customer_id
      join invoice_line l on l.invoice_id = i.id
      join product p on p.id = l.product_id
      join category t on t.id = p.category_id
    </select>

  </dao>
 
</hotrod>
```


### Prepare the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
drop table if exists invoice_line; 
drop table if exists invoice; 
drop table if exists customer;
drop table if exists product; 
drop table if exists category; 

create table customer (
  id int primary key not null,
  name varchar(15) not null
);

insert into customer (id, name) values
  (1, 'John Smith'),
  (2, 'Peter Ubisie'),
  (3, 'Anne With an E');

create table invoice (
  id int primary key not null,
  customer_id int references customer (id),
  purchase_date date,
  paid int
);

insert into invoice (id, customer_id, purchase_date, paid) values
  (1014, 1, date '2024-01-15', 1),
  (1015, 2, date '2024-01-16', 0),
  (1016, 2, date '2024-01-16', 1);

create table category (
  id int primary key not null,
  name varchar(10)
);

insert into category (id, name) values
  (50, 'Gadgets'),
  (51, 'Music');

create table product (
  id int primary key not null,
  name varchar(20),
  category_id int references category (id)
);

insert into product (id, name, category_id) values
  (20, 'Daguerrotype', 50),
  (21, 'Piano Forte', 51);

create table invoice_line (
  invoice_id int references invoice (id),
  product_id int references product (id),
  qty int
);

insert into invoice_line (invoice_id, product_id, qty) values
  (1014, 21, 1),
  (1015, 20, 1),
  (1015, 21, 2);
```


### Generate the Persistence Code

Now, let's use HotRod to generate the persistence code. Type:

```bash
mvn hotrod:gen
```

We see the code generation details:

```bash
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------------< com.myapp:myapp >---------------------------
[INFO] Building hello-nitro-graph 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- hotrod-maven-plugin:4.5.3 (default-cli) @ myapp ---
[INFO] HotRod version 4.5.3 (build 20240131-192313) - Generate
[INFO] 
[INFO] Configuration File: ./hotrod.xml
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
[INFO] Table CATEGORY included.
[INFO] Table CUSTOMER included.
[INFO] Table INVOICE included.
[INFO] Table INVOICE_LINE included.
[INFO] Table PRODUCT included.
[INFO] DAO WebDAO included.
[INFO]  - Select searchInvoices included.
[INFO]  
[INFO] Total of: 5 tables, 0 views, 0 enums, 1 DAO, and 0 sequences -- including 1 select method, and 0 query methods.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.938 s
[INFO] Finished at: 2024-01-31T14:23:24-05:00
[INFO] ------------------------------------------------------------------------

```

HotRod connected to the database schema, retrieved the table metadata, and produced the persistence layer:


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application 
class `src/main/java/app/App.java` as:

```java
package app;

import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.InvoiceWithLinesVO;
import app.daos.primitives.InvoiceQueriesDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  @Autowired
  private InvoiceQueriesDAO invoiceQueriesDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      helloNitroGraph();
      System.out.println("[ Example complete ]");
    };
  }

  private void helloNitroGraph() {
    List<InvoiceWithLinesVO> ivs = this.invoiceQueriesDAO.searchInvoices();
    ivs.forEach(i -> System.out.println("ivl=" + i.toJSON()));
  }

}
```


### Prepare the Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
# General configuration of the app

mybatis.mapper-locations=mappers/**/*.xml
logging.level.root=INFO

# Default datasource configuration

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

The Spring Boot application starts, connects to the database and run both queries. We see the result shown below:

```log
[ Starting example ]
ivl={ "id": 1014, "customerId": 1, "purchaseDate": "2024-01-15T05:00:00.000Z", "paid": 1, "customer": { "id": 1, "name": "John Smith" }, "lines": [ { "invoiceId": 1014, "productId": 21, "qty": 1, "q2": 2, "product": { "id": 21, "name": "Piano Forte", "categoryId": 51, "category": { "id": 51, "name": "Music" } } } ] }
ivl={ "id": 1015, "customerId": 2, "purchaseDate": "2024-01-16T05:00:00.000Z", "paid": 0, "customer": { "id": 2, "name": "Peter Ubisie" }, "lines": [ { "invoiceId": 1015, "productId": 20, "qty": 1, "q2": 2, "product": { "id": 20, "name": "Daguerrotype", "categoryId": 50, "category": { "id": 50, "name": "Gadgets" } } }, { "invoiceId": 1015, "productId": 21, "qty": 2, "q2": 4, "product": { "id": 21, "name": "Piano Forte", "categoryId": 51, "category": { "id": 51, "name": "Music" } } } ] }
[ Example complete ]
```

We can see:
- The Graph query finds two invoices, and returns them along with data from related tables (customer, products, categories).

That's it! You just generated the persistence code from the database and ran an app using it. Later on, when the
database suffers changes (it will), you can just rerun the generation step to apply the changes of columns, tables,
views, etc. to the persistence layer automatically.
