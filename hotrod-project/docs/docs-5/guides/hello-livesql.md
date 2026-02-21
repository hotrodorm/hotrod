# Hello LiveSQL

This guide shows a few examples of LiveSQL in action. It runs a Spring Boot project with Maven and H2 in-memory database.

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

If you are using a plain text editor (such as Notepad) you can create an empty folder and add
the files as described in the steps below. Alternatively, you can use your favorite IDE to create
a blank Maven project.

Create the following `pom.xml` file:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hello-livesql</artifactId>
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

    <dependency> <!-- Instantiates the JDBC DataSources -->
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>

    <dependency> <!-- HotRod -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>5.1.12</version>
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
        <version>5.1.12</version>
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

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

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
CREATE TABLE branch (
  id INT PRIMARY KEY NOT NULL,
  name VARCHAR(20),
  established DATE
);

INSERT INTO branch (id, name, established) VALUES
  (51, 'Central', date '2021-05-21'),
  (52, 'South', date '2022-02-22'),
  (54, 'North', date '2023-09-18'),
  (56, 'VIP', date '2024-10-12'),
  (57, 'Lakeside', date '2025-12-25');

CREATE TABLE employee (
  id INT GENERATED BY DEFAULT AS IDENTITY (start with 501) PRIMARY KEY NOT NULL,
  first_name VARCHAR(20),
  last_name VARCHAR(20),
  branch_id INT REFERENCES branch (id)
);

INSERT INTO employee (id, first_name, last_name, branch_id) VALUES
  (101457, 'Anne', 'Smith', 52),
  (609792, 'Steve', 'Locksmith', 56),
  (899288, 'Ronald', 'Kaminkow', 57),
  (134081, 'Alice', 'Badell', 51),
  (207121, 'Julia', 'Whitesmith', 52),
  (610043, 'John', 'Gardener', 54);
  
CREATE TABLE candidate (
  first_name VARCHAR(20),
  last_name VARCHAR(20),
  accepted INTEGER
);

INSERT INTO candidate (first_name, last_name, accepted) VALUES
  ('Marty', 'McFly', 1),
  ('Lorraine', 'Baines', 1),
  ('Biff', 'Tannen', 0),
  ('Emmett', 'Brown', 1);
```


### Generate the Persistence Layer

Let's generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

We see the layer generation details:

```bash
[INFO] --------------------------< com.myapp:myapp >---------------------------
[INFO] Building myapp 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- hotrod-maven-plugin:5.1.12:gen (default-cli) @ myapp ---
[INFO] HotRod Generator version 5.1.12 (build 20250520-023716) - Generate
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.2 (2.2.224 (2023-09-17))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.2 (2.2.224 (2023-09-17)) - implements JDBC Specification 4.2
[INFO] HotRod Adapter: H2 Adapter
[INFO]  
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO] Discover enabled.
[INFO]  
[INFO] Generating all facets.
[INFO]  
[INFO] Table BRANCH included.
[INFO] Table CANDIDATE included.
[INFO] Table EMPLOYEE included.
[INFO]  
[INFO] Total of: 3 tables, 0 views, 0 enums, 0 DAOs, and 0 sequences -- including 0 select methods, and 0 query methods.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.920 s
[INFO] Finished at: 2025-05-19T22:37:56-04:00
[INFO] ------------------------------------------------------------------------
```

## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application 
class `src/main/java/app/App.java` as:

```java
package app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.queries.InsertResult;
import org.hotrod.livesql.queries.select.tuples.gen.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.BranchDAO;
import app.persistence.dao.BranchDAO.BranchTable;
import app.persistence.dao.CandidateDAO;
import app.persistence.dao.CandidateDAO.CandidateTable;
import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Branch;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private CandidateDAO candidateDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoLiveSQLSelectRows();
      demoLiveSQLSelectTuples();
      demoLiveSQLInsertValues();
      demoLiveSQLInsertSelect();
      demoLiveSQLUpdate();
      demoLiveSQLDelete();
    };
  }

  private void demoLiveSQLSelectRows() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = sql
        .select()
        .from(e)
        .where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc())
        .execute();
    for (Row r : rows) {
      System.out.println("1. LiveSQL SELECT ROWS: " + r);
    }
  }

  private void demoLiveSQLSelectTuples() {
    EmployeeTable e = this.employeeDAO.newTable();
    BranchTable b = this.branchDAO.newTable();
    List<Tuple2<Employee, Branch>> tuples = sql
        .select()
        .tuples()
        .from(e).join(b, b.id.eq(e.branchId))
        .where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc())
        .execute();
    for (Tuple2<Employee, Branch> t : tuples) {
      System.out.println("2. LiveSQL SELECT TUPLES:");
      System.out.println("** Employee=" + t.getA());
      System.out.println("** Branch=" + t.getB());
    }
  }

  private void demoLiveSQLInsertValues() {
    EmployeeTable e = this.employeeDAO.newTable();
    int id = sql
        .insert(e)
        .columns(e.firstName, e.lastName, e.branchId)
        .values(sql.val("Bob"), sql.val("Marley"), sql.val(52))
        .execute();
    System.out.println("3. LiveSQL INSERT-VALUES -- generated id: " + id);
  }

  private void demoLiveSQLInsertSelect() {
    EmployeeTable e = this.employeeDAO.newTable();
    CandidateTable c = this.candidateDAO.newTable();
    InsertResult<Integer> r = sql
        .insert(e)
        .columns(e.firstName, e.lastName, e.branchId)
        .select(
            sql.select(c.firstName, c.lastName, sql.val(54))
               .from(c)
               .where(c.accepted.eq(1))
        )
        .execute();
    System.out.println("4. LiveSQL INSERT-SELECT -- count: " + r.getCount() + " -- generated ids: "
        + r.getKeys().stream().map(k -> "" + k).collect(Collectors.joining(",")));
  }

  private void demoLiveSQLUpdate() {
    EmployeeTable e = this.employeeDAO.newTable();
    Integer[] ids = new Integer[] { 51, 52, 53, 54, 70 };
    int count = sql
        .update(e)
        .set(e.branchId, 51)
        .where(e.lastName.lower().like("%smith%").and(e.branchId.in(ids)).and(e.branchId.eq(52)))
        .execute();
    System.out.println("5. LiveSQL UPDATE - count: " + count);
  }

  private void demoLiveSQLDelete() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = sql
        .delete(e)
        .where(e.branchId.eq(57))
        .execute();
    System.out.println("6. LiveSQL DELETE - count: " + count);
  }

}
```

The Hello LiveSQL app includes basic examples of LiveSQL SELECT, INSERT, UPDATE, and DELETE queries:

### The Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
```


### Run the Application

Let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs the queries. We see the ouput shown below:

```log
1. LiveSQL SELECT ROWS: {firstName=Steve, lastName=Locksmith, branchId=56, id=609792}
1. LiveSQL SELECT ROWS: {firstName=Anne, lastName=Smith, branchId=52, id=101457}

2. LiveSQL SELECT TUPLES:
** Employee=app.persistence.model.Employee@1fd7a37
- id=609792
- firstName=Steve
- lastName=Locksmith
- branchId=56
** Branch=app.persistence.model.Branch@5b202ff
- id=56
- name=VIP
- established=2024-10-12
2. LiveSQL SELECT TUPLES:
** Employee=app.persistence.model.Employee@58a84a12
- id=101457
- firstName=Anne
- lastName=Smith
- branchId=52
** Branch=app.persistence.model.Branch@e700eba
- id=52
- name=South
- established=2022-02-22

3. LiveSQL INSERT-VALUES -- generated id: 501

4. LiveSQL INSERT-SELECT -- count: 3 -- generated ids: 502,503,504

5. LiveSQL UPDATE - count: 2

6. LiveSQL DELETE - count: 1
```

We can see six sections identified by their numbers:

- The fisrt SELECT query found 2 *flat rows*, shown above
- The second SELECT query found 2 *tuples*, shown above
- The INSERT query with VALUES inserted a row and retrieved the primary key 501
- The INSERT query with a SELECT inserted three rows and retrieved the three primary keys 502, 503, and 504
- The UPDATE query updated two rows, according to the specified criteria
- The DELETE query deleted a single row

That's it! You ran four basic LiveSQL queries.

The LiveSQL syntax can include more complex query columns, predicates, expressions and other SQL clauses like WHERE, ORDER BY, GROUP BY, LIMIT, etc. See [LiveSQL](../livesql/README.md) for details.



