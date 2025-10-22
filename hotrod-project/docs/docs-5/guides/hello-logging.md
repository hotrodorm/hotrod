# Hello Logging

This guide runs a Spring Boot project with Maven and an H2 in-memory database to show how logging of executed SQL statements can be enabled.

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
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hello-logging</artifactId>
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
          <jdbcurl>jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1</jdbcurl>
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
CREATE TABLE employee (
  id int primary key not null,
  name varchar(20)
);

INSERT INTO employee (id, name) VALUES
  (10, 'Anne'),
  (11, 'Peter'),
  (12, 'Zoe');
```

### Configuring Persistence Layer

Create the file `layer.xml` with the following content:

```xml
<hotrod>

  <table name="employee" />

  <dao name="ReportsDAO">

    <select method="findEmployees" vo="FoundEmployee">
      <parameter name="txt" java-type="String" />
      SELECT *
      FROM employee
      WHERE name LIKE '%' || #{txt} || '%'
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
[INFO] Table EMPLOYEE included.
[INFO] DAO ReportsDAO included.
[INFO]  - Select findEmployees included.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.dao.ReportsDAO;
import app.persistence.model.Employee;
import app.persistence.model.FoundEmployee;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger LOG = Logger.getLogger(App.class.getName());

  private static final LiveSQLLogging LIVESQL_LOG = LiveSQLLogging.of(() -> LOG.isLoggable(Level.FINE),
      msg -> LOG.fine(msg));

  @Autowired
  private LiveSQL sql;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private ReportsDAO reportsDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoCRUD();
      demoNitroSelect();
      demoLiveSQL();
    };
  }

  private void demoCRUD() {
    Employee emp = this.employeeDAO.select(10);
    System.out.println("== Returns ==");
    System.out.println("1. Employee: " + emp);
  }

  private void demoNitroSelect() {
    List<FoundEmployee> fes = this.reportsDAO.findEmployees("t");
    System.out.println("== Returns ==");
    for (FoundEmployee fe : fes) {
      System.out.println("2. Found Employee: " + fe);
    }
  }

  private void demoLiveSQL() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = this.sql.select().from(e).where(e.name.like("%e")).execute(LIVESQL_LOG);
    System.out.println("== Returns ==");
    for (Row r : rows) {
      System.out.println("3. row=" + r);
    }
  }

}

```

### 5. The Runtime Properties File

The runtime properties are used when running the application. Create the file `./application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

# 1. One CRUD logger
logging.level.app.persistence.dao.EmployeeDAO=DEBUG

# 2. One Nitro logger
logging.level.app.persistence.dao.ReportsDAO=DEBUG

# 3. One logger used by LiveSQL
logging.level.app.App=DEBUG
```

The coniguration shows all three logging cases enabled, using the standard logging configuration of
Spring.

## Running the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs the three demo examples. The first one shows:

#### 1. The CRUD Select

This logger is activated by the configuration line:

```log
logging.level.app.persistence.dao.EmployeeDAO=DEBUG
```

The log shows:

```log
SELECT
  id,
  name
FROM employee
WHERE id = ?
```

#### 2. The Nitro Select

This logger is activated by the configuration line:

```log
logging.level.app.persistence.dao.ReportsDAO=DEBUG
```

The log shows:

```log
      SELECT *
      FROM employee
      WHERE name LIKE '%' || ? || '%'
```

#### 3. The LiveSQL Select

This logger is activated by the configuration line:

```log
logging.level.app.App=DEBUG
```

**Note**: Note that the class defines a `LIVESQL_LOG` adapter that LiveSQL uses to send logging info to your particular logging library.

The log shows:

```log
SELECT
  a.id as "id",
  a.name as "name"
FROM employee a
WHERE a.name like ?
--- Parameters (1) -------
 * 1 (java.lang.String, length=2): %e
--- Query Columns (2) ---
 * 1 id: java.lang.Integer, source: STATIC_DIALECT_RULE, rule #D9
 * 2 name: java.lang.String, source: STATIC_DIALECT_RULE, rule #D14
---------------------
```


That's it!

As you can see the logging can be enabled separately for CRUD, Nitro, and LiveSQL queries.





