# Starting a Maven Project from Scratch with H2 In-Memory Database

This guide creates a Spring Boot Maven project with HotRod persistence from scratch. Then, it runs it.

You'll need:
- Java.
- Maven.
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer.
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example.

This guide sets up the Maven project, creates a table in the database, generates the HotRod persistence from it, and then runs a simple application using it.

For the sake of simplicity this example combines settings in fewer files. For a production-like set up see
[using H2 persistent](./starting-a-maven-project-from-scratch-with-h2.md) or [using PostgreSQL](./starting-a-maven-project-from-scratch-with-postgresql.md) that separate the developer and DevOps roles more clearly.

For reference, after following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
pom.xml                    # The Maven project file
create-db.sql              # A SQL script that creates a table and data for this example
hotrod.xml                 # The HotRod configuration file
src/main/java              # The Java source code, including the generated DAOs and VOs
src/main/resources         # All resources including the generated mappers
src/main/resources/application.properties # Runtime properties
```

The Runtime Properties can override the Embedded Properties.

For simplicity, this guide creates these files in the main folder of the project. You can later change their locations to organize the project 
in your preferred way.


## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Set Up a Maven Project

If you are using a plain text editor (such as Notepad) you can create an empty folder and add the files as decribed in the 
steps below. Alternatively, you can use your favorite IDE to create a blank Maven project.

The `pom.xml` will include:
- The Spring Boot Starter dependency and the Spring Boot Plugin.
- The JDBC driver dependency according to your specific database. This is optional and can be provided at runtime.
- The HotRod, HotRod LiveSQL, and MyBatis Libraries. 
- The HotRod Generator Plugin.

For more details on how to configure the Maven dependencies and how to configure the HotRod generator plugin see [Maven Integration](../maven/maven.md). In short, the required libraries are:

With all these additions the complete `pom.xml` file will look like:

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

    <dependency> <!-- You can use Spring Boot, plain Spring, or other similar one -->
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>2.3.4.RELEASE</version>
    </dependency>  

    <dependency> <!-- Your app needs the JDBC driver to connect to the database. It could be also provided at runtime -->
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>2.1.214</version>
    </dependency>

    <dependency> <!-- Required. The main HotRod library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod</artifactId>
      <version>3.4.8</version>
    </dependency>

    <dependency> <!-- Required. HotRod's LiveSQL library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>3.4.8</version>
    </dependency>

    <dependency> <!-- Required. The generator uses MyBatis for database connectivity -->
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>2.1.3</version>
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
        <version>3.4.8</version>
        <configuration>
          <configfile>./hotrod.xml</configfile>
          <generator>MyBatis-Spring</generator>
          <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './create-db.sql';DB_CLOSE_DELAY=-1</jdbcurl>
          <jdbcusername>sa</jdbcusername>
          <jdbcpassword></jdbcpassword>
          <jdbccatalog>EXAMPLEDB</jdbccatalog>
          <jdbcschema>PUBLIC</jdbcschema>
          <facets></facets>
          <display></display>
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

Create the empty source folders, if they are not yet created. In linux you can do:

```bash
mkdir -p src/main/java
mkdir -p src/main/resources
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

Finally, run Maven once, using `mvn clean compile` to make sure the `pom.xml` file is correctly formed. 


## Part 2 &mdash; Creating a Table and Generating the Persistence Code

In this section we create an in-memory table in H2 database and we generate the persistence code from it.

### Create a Table in the Database

Create the file `create-db.sql` with the following SQL content:

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


### Create the HotRod Configuration File

Tell HotRod how you want the generation to work. Create the file `hotrod.xml` (in the folder of
your choosing and with any name) and add:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod SYSTEM "hotrod.dtd">

<hotrod>

  <generators>
    <mybatis-spring>
      <daos package="com.myapp.daos" 
        dao-prefix=""  dao-suffix="DAO" vo-prefix=""  vo-suffix="Impl" abstract-vo-prefix=""  abstract-vo-suffix="VO" 
        ndao-prefix="" ndao-suffix=""   nvo-prefix="" nvo-suffix=""  nabstract-vo-prefix="" nabstract-vo-suffix=""
      />
      <mappers dir="mappers" />
      <classic-fk-navigation />
      <select-generation strategy="result-set" />          
    </mybatis-spring>
  </generators>
  
  <table name="employee" />

</hotrod>
```

### Generate the Persistence Code

Now, let's use HotRod to generate the persistence code. Type:

`mvn hotrod:gen`

We see the code generation details:

```bash
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------------< com.myapp:myapp >---------------------------
[INFO] Building myapp 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- hotrod-maven-plugin:3.4.8 (default-cli) @ myapp ---
[INFO] HotRod version 3.4.8 (build 20221102-152614) - Generate
[INFO] 
[INFO] Configuration File: ~/example/./hotrod.xml
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './create-db.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.1 (2.1.214 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.1 (2.1.214 (2022-06-13)) - implements JDBC Specification 4.2
[INFO] Database Adapter: H2 Adapter
[INFO] 
[INFO] Current Catalog: EXAMPLEDB
[INFO] Current Schema: PUBLIC
[INFO] 
[INFO] Generating all facets.
[INFO]  
[INFO] Table EMPLOYEE included.
[INFO]  
[INFO] Total of: 1 table, 0 views, 0 enums, 0 DAOs, and 0 sequences -- including 0 select methods, and 0 query methods.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.826 s
[INFO] Finished at: 2022-11-02T11:54:34-04:00
[INFO] ------------------------------------------------------------------------
```

HotRod connected to the database schema, will retrieve the table details, and will produce the code. It will create or update the following files:
* `src/main/java/com/myapp/daos/EmployeeImpl.java`
* `src/main/java/com/myapp/daos/primitives/EmployeeDAO.java`
* `src/main/java/com/myapp/daos/primitives/EmployeeVO.java`
* `src/main/resources/mappers/primitives-employee.xml`

Note that since the `EmployeeImpl.java` may contain custom code, it's created the first time but never overwritten afterwards. The other files are always overwritten to keep them current with the latest database structure.


## Part 3 &mdash; The Application

### A Simple Spring Boot Application

Let's write a simple application that perform two searches in the table we created before. Create the application 
class `src/main/java/com/myapp/App.java` as:

```java
package com.myapp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.myapp.daos.primitives.EmployeeDAO;
import com.myapp.daos.primitives.EmployeeDAO.EmployeeTable;
import com.myapp.daos.primitives.EmployeeVO;
import com.myapp.daos.EmployeeImpl;

@Configuration
@ComponentScan(basePackageClasses = App.class)
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@SpringBootApplication
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
      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() {

    // Use CRUD to search for employee #123

    Integer id = 123;
    EmployeeVO vo = this.employeeDAO.selectByPK(id);
    System.out.println("Employee #" + id + " Name: " + vo.getName());

    // Use LiveSQL to search for employees whose name starts with 'A'

    EmployeeTable e = EmployeeDAO.newTable();

    List<Map<String, Object>> l = this.sql
      .select()
      .from(e)
      .where(e.name.like("A%"))
      .execute();

    System.out.println("Employees with names that start with 'A':");
    for (Map<String, Object> r: l) {
      System.out.println(r);
    }

  }

}
```

Now, let's prepare the properties files. Spring properties are divided in two groups (at least):
- Embedded properties that will be included as part of the jar application to be deployed.
- External properties set up by DevOps (as a separate file) when deploying the application in production or any environment.

### Prepare the Runtime Properties File

The runtime properties are used when running the application. Create the file `src/main/resources/application.properties` as:

```properties
mybatis.mapper-locations=mappers/**/*.xml
logging.level.root=INFO
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './create-db.sql';DB_CLOSE_DELAY=-1
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
Employee #123 Name: Alice
Employees with names that start with 'A':
{name=Anne, id=45}
{name=Alice, id=123}
[ Example complete ]
```
