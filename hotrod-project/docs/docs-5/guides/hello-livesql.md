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
      <version>5.1.4</version>
    </dependency>

    <dependency> <!-- The JDBC driver to connect to the database; can be provided at runtime -->
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
        <version>5.1.4</version>
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
create table employee (
  id int primary key not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  branch_id
);

insert into employee (id, first_name, last_name, branch_id) values
  (101457, 'Anne', 'Smith', 2),
  (609792, 'Steve', 'Locksmith', 6),
  (899288, 'Ronald', 'Kaminkow', 7),
  (134081, 'Alice', 'Badell', 1),
  (207121, 'Julia', 'Whitesmith', 2),
  (610043, 'John', 'Gardener', 4);
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
[INFO] --- hotrod-maven-plugin:5.0.0:gen (default-cli) @ myapp ---
[INFO] HotRod Generator version 5.0.0 (build 20250520-023716) - Generate
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.1 (2.1.214 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.1 (2.1.214 (2022-06-13)) - implements JDBC Specification 4.2
[INFO] HotRod Adapter: H2 Adapter
[INFO]  
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO] Discover enabled.
[INFO]  
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

import java.util.List;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoLiveSQLSelect();
      demoLiveSQLInsert();
      demoLiveSQLUpdate();
      demoLiveSQLDelete();
    };
  }

  private void demoLiveSQLSelect() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = this.sql
        .select()
        .from(e)
        .where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc())
        .execute();
    for (Row r : rows) {
      System.out.println("1. LiveSQL SELECT - selected row: " + r);
    }
  }

  private void demoLiveSQLInsert() {
    Employee emp = new Employee();
    emp.setFirstName("Bob");
    emp.setLastName("Marley");
    emp.setBranchId(420);
    Employee inserted = this.employeeDAO.insert(emp);
    System.out.println("2. LiveSQL INSERT - inserted row: " + inserted);
  }

  private void demoLiveSQLUpdate() {
    EmployeeTable e = this.employeeDAO.newTable();
    Employee values = new Employee();
    values.setBranchId(15);
    int count = this.employeeDAO
        .update(values, e, e.lastName.lower().like("%smith%").and(e.branchId.eq(2)))
        .execute();
    System.out.println("3. LiveSQL UPDATE - updated rows: " + count);
  }

  private void demoLiveSQLDelete() {
    Employee example = new Employee();
    example.setBranchId(7);
    int count = this.employeeDAO.delete(example);
    System.out.println("4. LiveSQL DELETE - deleted rows: " + count);
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
1. LiveSQL SELECT - selected row: {firstName=Steve, lastName=Locksmith, branchId=6, id=609792}
1. LiveSQL SELECT - selected row: {firstName=Anne, lastName=Smith, branchId=2, id=101457}

2. LiveSQL INSERT - inserted row: app.persistence.model.Employee@664632e9
- id=1
- firstName=Bob
- lastName=Marley
- branchId=420

3. LiveSQL UPDATE - updated rows: 2

4. LiveSQL DELETE - deleted rows: 1
```

We can see four sections identified by their numbers:

- The SELECT query found 2 rows, shown above
- The INSERT query inserted a single row with autogenerated primary key (id=1)
- The UPDATE query updated 2 rows, according to the specified criteria
- The DELETE query deleted a single row

That's it! You ran four basic LiveSQL queries.

The LiveSQL syntax can include more complex query columns, predicates, expressions and other SQL clauses like WHERE, ORDER BY, GROUP BY, LIMIT, etc. See [LiveSQL](../livesql/README.md) for details.



