# Hello Identifiers

This guide runs a Spring Boot project with Maven and H2 in-memory database. It shows how to use non-ASCII identifiers or identifiers that correspond to reserved words in the specific database.

You'll need:

- Java
- Maven
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example

This guide sets up the Maven project, creates a table in the database, generates the HotRod persistence from it, and then runs a simple application using it.

After following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
pom.xml                    # The Maven project file
schema.sql                 # A SQL script that creates a table and data for this example
src/main/java              # Your Java app and the generated DAOs and VOs
application.properties     # The runtime properties
```


## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Set Up a Maven Project

If you are using a plain text editor (such as Notepad) you can create an empty folder and add the files as described in the
steps below. Alternatively, you can use your favorite IDE to create a blank Maven project.

The `pom.xml` will include:

- The Spring Boot Starter dependency and the Spring Boot Plugin
- The JDBC driver dependency according to your specific database
- The HotRod Libraries
- The HotRod Generator Plugin

The complete `pom.xml` file will look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hello-identifiers</artifactId>
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

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>5.1.18</version>
    </dependency>

    <dependency> <!-- The JDBC driver to connect to the database; can be provided at runtime -->
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
        <version>5.1.18</version>
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
            <version>2.2.224</version>
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

Change the command above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Generating the Persistence Layer

In this part we create an in-memory table in H2 database and we generate the persistence layer from it.


### Prepare the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
DROP TABLE IF EXISTS "Order Type α";

CREATE TABLE "Order Type α" ( -- the table name has mixed-case letters, spaces, and a greek letter
  id int primary key,
  "case" VARCHAR(200) -- this column name is a reserved word in PostgreSQL
);

INSERT INTO "Order Type α" (id, "case") VALUES
  (1, 'Type Alpha'),
  (2, 'Beta'),
  (3, 'Gamma');
```

### Declare the Table and the Column

This table and column declaration is necessary since their names need to be aliased to be Java compliant. Otherwise your app code won't compile.

The table and column declaration can be combined with schema discovery, where all other schema tables are discovered automatically and the ones explicitly declared supersede their default names.

This example shows how non-typical identifiers can be used in the persistence layer. These include:

- **Identifiers with non-alphanumeric ASCII characters**: the table name includes the letter alpha `α` (non-ASCII)  and also a space (non-alphanumeric)
- **Mixed-case identifiers**: the table name include upper case and lower case letters, as in `Type`.
- **Reserved words**: a column in the table has the name `case`, that is a reserved word in H2. This identifier can still be used for a table, view, or column but needs be escaped.

Create the `layer.xml` file with:

```xml
<hotrod>

  <generators>
    <jdbc/>
  </generators>

  <table name="'Order Type α'" entity="OrderTypeA">
    <column name="'case'" property="orderCase" />
  </table>

</hotrod>
```

Note that the special names for the table and column are enclosed in single quotes. This tells the persistence layer to use them as literal identifiers "as is".

### Generate the Persistence Layer

Now, let's use HotRod to generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

We see the layer generation details:

```bash
[INFO] --------------------------< com.myapp:myapp >---------------------------
[INFO] Building myapp 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- hotrod-maven-plugin:5.1.18:gen (default-cli) @ myapp ---
[INFO] HotRod Generator version 5.1.18 (build 20250520-023716) - Generate
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.2 (2.2.224 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.2 (2.2.224 (2022-06-13)) - implements JDBC Specification 4.2
[INFO] HotRod Adapter: H2 Adapter
[INFO]  
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO]  
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.920 s
[INFO] Finished at: 2025-05-19T22:37:56-04:00
[INFO] ------------------------------------------------------------------------
```

HotRod connected to the database schema, discovered the table details and generated the persistence layer. In sum, it created the following files:

## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application 
class `src/main/java/app/App.java` as:

```java
package app;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.BranchDAO;
import app.persistence.dao.BranchDAO.BranchTable;
import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      demoCRUD();
      demoLiveSQL();
    };
  }

  private void demoCRUD() throws DynamicExpressionException, SQLException {
    Employee emp = this.employeeDAO.select(134081);
    System.out.println("Employee #123081's name: " + emp.getFirstName());
  }

  private void demoLiveSQL() {

    System.out.println("Employees with last names that include smith from branches of type 2, 6, or 7:");

    EmployeeTable e = this.employeeDAO.newTable("e");
    BranchTable b = this.branchDAO.newTable("b");

    List<Row> rows = this.sql
      .select(e.star(), b.name.as("branchName"))
      .from(e)
      .join(b, b.id.eq(e.branchId))
      .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
      .orderBy(b.name, e.lastName.desc())
      .execute();

    for (Row r : rows) {
      System.out.println(r);
    }

  }

}
```

The LiveSQL query above executes the following query:

```sql
  SELECT e.*, b.name AS "branchName"
  FROM employee e
  JOIN branch b ON b.id = e.branch_id
  WHERE lower(e.last_name) LIKE '%smith%' AND b.type IN (2, 6, 7)
  ORDER BY b.name, e.last_name DESC
```

### Prepare the Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
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

The Spring Boot application starts, connects to the database and runs both queries. We see the result shown below:

```log
[ Starting example ]
Employee #123081's name: Alice
Employees with last names that include smith from branches of type 2, 6, or 7:
{firstName=Steve, lastName=Locksmith, branchId=6, branchName=East Coast, id=609792}
{firstName=Julia, lastName=Whitesmith, branchId=2, branchName=North, id=207121}
{firstName=Anne, lastName=Smith, branchId=2, branchName=North, id=101457}
[ Example complete ]
```

We can see:
- The CRUD query `select id, name from employee where id = 123081` was run and returned 1 row.
- The LiveSQL query returned 3 rows using the correct search criteria and ordering.

That's it! You just generated the persistence layer from the database and ran an app using it.

Later on, when the database suffers changes &mdash; it will &mdash; you can just
rerun the generation step `mvn hotrod:gen` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
