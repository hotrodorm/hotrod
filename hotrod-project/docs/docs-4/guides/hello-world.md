# Hello World!

This guide runs a Spring Boot project with Maven and H2 in-memory database. It shows the basic idea of how HotRod works.

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

  <groupId>com.myapp</groupId>
  <artifactId>myapp</artifactId>
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
      <version>4.0.0</version>
    </dependency>

    <dependency> <!-- Required. HotRod's LiveSQL library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>4.0.0</version>
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
        <version>4.0.0</version>
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
mkdir -p src/main/java
mkdir -p src/main/resources
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Generating the Persistence Code

In this part we create an in-memory table in H2 database and we generate the persistence code from it.


### Prepare the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
drop table if exists employee; 

create table employee (
  id int primary key not null,
  name varchar(20) not null
);

insert into employee (id, name) values (45, 'Anne');
insert into employee (id, name) values (123, 'Alice');
insert into employee (id, name) values (6097, 'Steve');
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
[INFO] Building myapp 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- hotrod-maven-plugin:4.0.0 (default-cli) @ myapp ---
[INFO] HotRod version 4.0.0 (build 20221102-152614) - Generate
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.1 (2.1.214 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.1 (2.1.214 (2022-06-13)) - implements JDBC Specification 4.2
[INFO] Database Adapter: H2 Adapter
[INFO] 
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO] Discover enabled.
[INFO] 
[INFO] Generating all facets.
[INFO]  
[INFO] Table EMPLOYEE included.
[INFO]  
[INFO] Total of: 1 table, 0 views, 0 enums, 0 DAOs, and 0 sequences
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.826 s
[INFO] Finished at: 2022-11-02T11:54:34-04:00
[INFO] ------------------------------------------------------------------------
```

HotRod connected to the database schema, retrieved the table details, and produced the persistence code. It created the following files:

* `src/main/java/app/daos/EmployeeVO.java`
* `src/main/java/app/daos/primitives/EmployeeDAO.java`
* `src/main/java/app/daos/primitives/Employee.java`
* `src/main/resources/mappers/primitives-employee.xml`

Note that since the `EmployeeVO.java` is designed to include custom code, it's never 
overwritten. The other files are always overwritten to keep them current with the latest database structure.


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application 
class `src/main/java/app/App.java` as:

```java
package app;

import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.EmployeeVO;
import app.daos.primitives.EmployeeDAO;
import app.daos.primitives.EmployeeDAO.EmployeeTable;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
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
      System.out.println("[ Starting example ]");
      demo();
      System.out.println("[ Example complete ]");
    };
  }

  private void demo() {

    // 1. Use CRUD to search for employee #123

    Integer id = 123;
    EmployeeVO vo = this.employeeDAO.select(id);
    System.out.println("Employee #" + id + " Name: " + vo.getName());

    // 2. Use LiveSQL to search for employees whose name starts with 'A'

    System.out.println("Employees with names that start with 'A':");
    EmployeeTable e = EmployeeDAO.newTable("e");

    List<Row> rows = this.sql
        .select()
        .from(e)
        .where(e.name.like("A%"))
        .execute();

    rows.stream().forEach(r -> System.out.println(r));

    // 3. Add a configuration file (not included in this example) to define Nitro queries

  }

}
```


### Prepare the Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
# General configuration of the app

logging.level.root=INFO

# Default datasource configuration

spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
mybatis.mapper-locations=mappers/**/*.xml
```


### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and run both queries. We see the result shown below:

```log
[ Starting example ]
Employee #123 Name: Alice
Employees with names that start with 'A':
{name=Anne, id=45}
{name=Alice, id=123}
[ Example complete ]
```

We can see:
- The CRUD SQL statement `select id, name from employee where id = 123` was run and returned 1 row.
- The LiveSQL SQL statement `SELECT * FROM employee WHERE name like 'A%'` was run and returned 2 rows.

That's it! You just generated the persistence code from the database and ran an app using it. Later on, when the
database suffers changes (it will), you can just rerun the generation step to apply the changes of columns, tables,
views, etc. to the persistence code automatically.
