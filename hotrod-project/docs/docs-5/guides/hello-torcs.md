# Hello Torcs

This guide runs a Spring Boot project with Maven and H2 in-memory database. It shows the basic idea of how HotRod works.

You'll need:

- Java
- Maven
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example

This guide sets up the Maven project and runs Torcs in it. The app executes a few lightweight and heavier queries in the database, and then retrieves a query execution ranking using Torcs.

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
  <artifactId>hello-torcs</artifactId>
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
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-torcs</artifactId>
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
  branch_id int
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
[INFO] --- hotrod-maven-plugin:5.1.4:gen (default-cli) @ myapp ---
[INFO] HotRod Generator version 5.1.4 (build 20250520-023716) - Generate
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

Create the application class `src/main/java/app/App.java` as:

```java
package app;

import java.util.List;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.rankings.RankingEntry;
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
  private Torcs torcs;

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
      runLightweightSelects1();
      runLightweightSelects2();
      runHeavySelects1();
      runHeavySelects2();
      showRanking();
    };
  }

  private void runLightweightSelects1() {
    int total = 0;
    for (int x = 0; x < 10; x++) {
      Employee emp = this.employeeDAO.select(134081 + x);
      total = total + (emp == null ? 0 : 1);
    }
    System.out.println("Lightweight 1 - rows: " + total);
  }

  private void runLightweightSelects2() {
    int total = 0;
    EmployeeTable e = this.employeeDAO.newTable();
    for (int x = 0; x < 10; x++) {
      List<Employee> rows = this.employeeDAO.select(e, e.firstName.like("%a%").and(e.lastName.length().gt(5)))
          .execute();
      total = total + rows.size();
    }
    System.out.println("Lightweight 1 - rows: " + total);
  }

  private void runHeavySelects1() {
    for (int x = 0; x < 5; x++) {
      // Doing cross join between 6 instances of the same table (~46k rows)
      EmployeeTable e = this.employeeDAO.newTable();
      EmployeeTable f = this.employeeDAO.newTable();
      EmployeeTable g = this.employeeDAO.newTable();
      EmployeeTable h = this.employeeDAO.newTable();
      EmployeeTable i = this.employeeDAO.newTable();
      EmployeeTable j = this.employeeDAO.newTable();
      List<Row> rows = this.sql.select(e.id).from(e).crossJoin(f).crossJoin(g).crossJoin(h).crossJoin(i).crossJoin(j)
          .execute();
      System.out.println("Heavy 1 - rows=" + rows.size());
    }
  }

  private void runHeavySelects2() {
    for (int x = 0; x < 2; x++) {
      // Doing cross join between 8 instances of the same table (~1.6M rows)
      EmployeeTable e = this.employeeDAO.newTable();
      EmployeeTable f = this.employeeDAO.newTable();
      EmployeeTable g = this.employeeDAO.newTable();
      EmployeeTable h = this.employeeDAO.newTable();
      EmployeeTable i = this.employeeDAO.newTable();
      EmployeeTable j = this.employeeDAO.newTable();
      EmployeeTable k = this.employeeDAO.newTable();
      EmployeeTable l = this.employeeDAO.newTable();
      List<Row> rows = this.sql.select(e.firstName).from(e).crossJoin(f).crossJoin(g).crossJoin(h).crossJoin(i)
          .crossJoin(j).crossJoin(k).crossJoin(l).execute();
      System.out.println("Heavy 2 - rows=" + rows.size());
    }
  }

  private void showRanking() {
    System.out.println("Ranking:");
    for (RankingEntry re : this.torcs.getDefaultRanking().getEntries()) {
      System.out.println(re);
    }
  }

}
```

Create the configuration class `src/main/java/app/MyConfiguration.java` to wrap the DataSource in Torcs DataSource as:

```java
package app;

import javax.sql.DataSource;

import org.hotrod.torcs.decorators.TorcsDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSource(DataSourceProperties properties) {
    return new TorcsDataSource(properties.initializeDataSourceBuilder().build());
  }

}
```

### Prepare the Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
```

**Note**: In order to wrap the DataSource in the Torcs DataSource, the autoconfig features for the data source needs to be disabled. The DataSource instantiation is now done in the `MyConfiguration` class.


### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs both queries. We see the result shown below:

```log
total selected rows: 1
Heavy 1 - rows=46656
Heavy 1 - rows=46656
Heavy 1 - rows=46656
Heavy 1 - rows=46656
Heavy 1 - rows=46656
Heavy 2 - rows=1679616
Heavy 2 - rows=1679616

Ranking:
2 executions, 0 errors, avg 509 ms, σ 22 [487-531 ms], impact 1018 ms, executed: Thu Oct 23 13:22:42 EDT 2025 - Thu Oct 23 13:22:43 EDT 2025, last exception: N/A -- [ds0] SELECT a.first_name as "firstName" FROM employee a CROSS JOIN employee b CROSS JOIN employee c CROSS JOIN employee d CROSS JOIN employee e CROSS JOIN employee f CROSS JOIN employee g CROSS JOIN employee h
5 executions, 0 errors, avg 7 ms, σ 10 [0-25 ms], impact 38 ms, executed: Thu Oct 23 13:22:41 EDT 2025 - Thu Oct 23 13:22:41 EDT 2025, last exception: N/A -- [ds0] SELECT a.id as "id" FROM employee a CROSS JOIN employee b CROSS JOIN employee c CROSS JOIN employee d CROSS JOIN employee e CROSS JOIN employee f
10 executions, 0 errors, avg 0 ms, σ 1 [0-3 ms], impact 5 ms, executed: Thu Oct 23 13:26:29 EDT 2025 - Thu Oct 23 13:26:29 EDT 2025, last exception: N/A -- [ds0] SELECT id, first_name, last_name, branch_id FROM employee WHERE id = ?
10 executions, 0 errors, avg 0 ms, σ 1 [0-1 ms], impact 5 ms, executed: Thu Oct 23 13:26:29 EDT 2025 - Thu Oct 23 13:26:29 EDT 2025, last exception: N/A -- [ds0] SELECT a.id, a.first_name, a.last_name, a.branch_id FROM employee a WHERE a.first_name like ? and length(a.las
```

This example shows a simple output that dump the ranking information. We can format this in a table as:

| Rank | Execs | Errors | Avg Time (ms) | Observed Time (ms) | Impact (ms) | Data Source | SQL |
| :--: | --:| --:| --:| --:| --:| :--: | :-- |
| #1 | 2     |   0 |      509 | 487-531                      | 1018         | #0 | SELECT a.first_name as "firstName" FROM employee a CROSS JOIN employee b... |
| #2 | 5     |   0 |        7 | 0-25                         | 38           | #0 | SELECT a.id as "id" FROM employee a CROSS JOIN employee b... |
| #3 | 10    |   0 |        0 | 0-3                          | 5            | #0 | SELECT id, first_name, last_name, branch_id FROM employee WHERE id = ? |
| #4 | 10    |   0 |        0 | 0-1                          | 5            | #0 | SELECT SELECT a.id, a.first_name, a.last_name, a.branch_id FROM employee a WHERE a.first_name like ? and length(a.last_name) <= ? |

We can see:

- We used the default ranking, per highest response time. The entries are ranked by it; that is by 531, 25, 3, 1 ms respectively
- We see four entries; even though we executed each query many times, Torcs records the stats for each one in a single entry
- Torcs clearly identified the query "Heavy 2" as the slowest one
- One of the most important values in this table is the "Impact", i.e. total of elapsed times. A fast query that is executed a million time can have a higher impact than a slow query that is executed only once
- This table does not include some columns that are less used, but can also be important. For example the time frame when a query is run could be significant in some cases

That's it! You just ran multiple queries and used Torcs to see their stats.


