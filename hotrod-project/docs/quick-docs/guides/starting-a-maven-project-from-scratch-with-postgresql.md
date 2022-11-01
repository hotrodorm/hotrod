# Starting a Maven Project from Scratch with PostgreSQL Database

This guide creates a Spring Boot Maven project with HotRod persistence from scratch. Then, it runs it.

You'll need:
- Java.
- Maven.
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer.
- A running database. This example uses PostgreSQL, but this can be easily changed to another database.

This guide sets up the Maven project, creates a table in the database, generates the HotRod persistence from it, and then runs a simple application using it.

For reference, after following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
pom.xml                    # The Maven project file
hotrod.xml                 # The HotRod configuration file
hotrod.properties          # The HotRod Local properties
src/main/java              # The Java source code, including the generated DAOs and VOs
src/main/resources         # All resources including the generated mappers
src/main/resources/application.properties # Embedded properties, configured by the developer
application.properties     # The runtime properties, configured by DevOps in the production deployment. They override the embedded properties
```

For simplicity, this guide creates these files in the main folder of the project. You can later change their locations to organize the project 
in your preferred way.


## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Set Up a Maven Project

If you are using a plain text editor (such as Notepad) you can create an empty folder and add the files as decribed in the steps below.

Alternatively, you can use your favorite IDE to create a blank Maven project.

The `pom.xml` will include:
- The Spring Boot Starter dependency and the Spring Boot Plugin.
- The JDBC driver dependency according to your specific database. This is optional and can be provided at runtime.
- The HotRod, HotRod LiveSQL, and MyBatis Libraries. 
- The HotRod Generator Plugin.

For more details on how to configure the Maven dependencies and how to configure the HotRod generator plugin see [Maven Integration](../maven/maven.md). In short, the required libraries are:

| Library | Description |
| -- | -- |
| `org.hotrodorm.hotrod:hotrod` | The core library needed at runtime |
| `org.hotrodorm.hotrod:livesql` | Implements LiveSQL |
| `org.mybatis.spring.boot:mybatis-spring-boot-starter` | MyBatis database connectivity layer |

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
    <springboot.version>2.3.4.RELEASE</springboot.version>
    <hotrod.version>3.4.8</hotrod.version>
    <mybatis.version>2.1.3</mybatis.version>
  </properties>

  <dependencies>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>${springboot.version}</version>
    </dependency>  

    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.2.5</version>
    </dependency>

    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod</artifactId>
      <version>${hotrod.version}</version>
    </dependency>

    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>${hotrod.version}</version>
    </dependency>

    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>${mybatis.version}</version>
    </dependency>    

  </dependencies>

  <build>

    <plugins>

      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${springboot.version}</version>
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
        <version>${hotrod.version}</version>
        <configuration>
          <localproperties>hotrod.properties</localproperties>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.5</version>
          </dependency>
        </dependencies>
      </plugin>

    </plugins>

  </build>

</project>
```

**Note**: Don't forget to change JDBC driver (in two places) according the database you are using.

Create the empty source folders, if they are not yet created. In linux you can do:

```bash
mkdir -p src/main/java
mkdir -p src/main/resources
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

Finally, run Maven once, using `mvn clean` to make sure the `pom.xml` file is correctly formed.

## Part 2 &mdash; Creating a Table and Generating the Persistence Code

In this section we create a table in the database and we generate the persistence code for Spring Boot.

### Create a Table in the Database

At this point we'll need a database. We'll assume the database, schema, and permissions are ready to be used, and that we
know the connection details to it.

Let's create a simple table in the database. Use your favorite database client and run the following SQL statements:

```sql
create table employee (
  id int primary key not null,
  name varchar(20) not null
);

insert into employee (id, name) values (45, 'Anne');
insert into employee (id, name) values (123, 'Alice');
insert into employee (id, name) values (6097, 'Steve');
```

Great. We now have a table in the database with three rows of data in it.

### Generate the Persistence Code

#### The HotRod Configuration File

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

In the file above:
- We can change the package name of the daos according to the specific project, and we can change the DAOs 
and VOs prefixes and suffixes as needed. We can also change the mappers directory, to generate them to a different folder.
- It's recommended to keep the `<classic-fk-navigation />` and `<select-generation strategy="result-set" />` tags to enable modern features.
- Finally, we see the list of tables we want to inspect. In this case this list only includes a single table: `employee`.

#### The HotRod Properties File

Now, let's create the configuration file `hotrod.properties` (referenced by the `pom.xml`) so the HotRod Generator can connect to the sandbox database. Create this file with the following content:

```properties
configfile=./hotrod.xml
generator=MyBatis-Spring
jdbcdriverclass=org.postgresql.Driver
jdbcurl=jdbc:postgresql://192.168.56.214:5432/mydatabase
jdbcusername=myusername
jdbcpassword=mypassword
jdbccatalog=
jdbcschema=public
facets=
display=
```

Change the URL, username, password, and schema as needed to match your current database.

**Note**: The connection details included in this file are used for persistence generation purposes only. They typically point to a sandbox database in the 
development environment. When running in the production environment HotRod will pick up the connection details from the `datasource` Spring bean, typically
configured in the `application.properties` file of the application.

**Note**: Also, depending on the project organization, developers may not commit the `hotrod.properties` to the repository. This is typically the case
for distributed teams where each one of the developers uses a local independent sandbox database with a different URL or credentials. In the case of 
centralized teams where all developers use the same sandbox database, then this file can have the same values, and may be safely commited to the source code repository.

#### Generate the Persistence Code

Now, let's use HotRod to generate the persistence code. Type:

`mvn hotrod:gen`

HotRod will connect to the database schema, will retrieve the table details, and will produce the code. It will create or update the following files:
* `src/main/java/com/myapp/daos/EmployeeImpl.java`
* `src/main/java/com/myapp/daos/primitives/EmployeeDAO.java`
* `src/main/java/com/myapp/daos/primitives/EmployeeVO.java`
* `src/main/resources/mappers/primitives-employee.xml`

**Note**: Since the `EmployeeImpl.java` may contain custom code, it's created but never overwritten. The other files are always overwritten to keep them current with the latest database structure.

At this point all the persistence code is ready to be used.

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

### Prepare the Embedded Properties File

Embeded properties define the default values for Spring, and they will be included in the jar file when building it. 
Create the file `src/main/resources/application.properties` as:

```properties
mybatis.mapper-locations=mappers/**/*.xml
logging.level.root=INFO
```

The first property tells Spring about the location of the mappers. Change accordingly if you change the `hotrod.xml` configuration file.
The second property sets up the default level of logging. There are a myriad of other Spring properties that can be set up here, and that 
depends on the specifics of the project.

### Prepare the External Properties File

External properties are meant for the DevOps team to tweak, since they will need to provide specific production details to the application.
In particular, DevOps will need to set up the database connection details. Place here any other details that will need to be managed by DevOps.

Create the file `application.properties` (same name as before but this time in the main dir) as:

```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://192.168.56.214:5432/mydatabase
spring.datasource.username=myusername
spring.datasource.password=mypassword
```

**Note**: Change the values above according to your specific database.

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

### Run the Application with SQL Debugging Active

If you want to see the exact SQL queries that are run in the database you can activate the logging for it. Edit the `application.properties` file 
and append the following properties to it:

```properties
logging.level.com.myapp.daos.primitives.employee.selectByPK=DEBUG
logging.level.org.hotrod.runtime.livesql.LiveSQLMapper=DEBUG
```

The first property activates the SQL log for the `selectByPK()` method of the `EmployeeDAO`. The second property activates the SQL log for all LiveSQL
queries. A `DEBUG` level shows the SQL statement and the applied parameters, while a `TRACE` level will also include all the selected data; use 
the `TRACE` level with caution since it can add a massive amount of logging to your log files.

**Note**: while there are separated loggers for each CRUD method, there's a single logger for all LiveSQL queries.

Let's run the application again. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and run both queries. We see the result shown below:

```log
[ Starting example ]
DEBUG --- [main] c.m.daos.primitives.employee.selectByPK  : ==>  Preparing: select id, name from employee where id = ?
DEBUG --- [main] c.m.daos.primitives.employee.selectByPK  : ==> Parameters: 123(Integer)
DEBUG --- [main] c.m.daos.primitives.employee.selectByPK  : <==      Total: 1
Employee #123 Name: Alice
DEBUG --- [main] o.h.r.livesql.LiveSQLMapper.select       : ==>  Preparing: SELECT * FROM employee WHERE name like 'A%'
DEBUG --- [main] o.h.r.livesql.LiveSQLMapper.select       : ==> Parameters: 
DEBUG --- [main] o.h.r.livesql.LiveSQLMapper.select       : <==      Total: 2
Employees with names that start with 'A':
{name=Anne, id=45}
{name=Alice, id=123}
[ Example complete ]
```

This time we see:
- The CRUD SQL statement `select id, name from employee where id = ?`, the applied parameter `123`, and the number of returned rows `1`.
- The LiveSQL SQL statement `SELECT * FROM employee WHERE name like 'A%'` and the number of returned rows `2`.

DevOps can tweak the logging properties (or any other property) in production, if we need extra information about the actual queries being run.

### Packaging the Application for Deployment into Production

Now that the application is tested we can package it for a production deployment. Run:

```bash
mvn clean package
```

Maven builds the applications and produces a single jar file at `target/myapp-1.0.0-SNAPSHOT.jar`. The name is assembled
with the values in the `<artifactId>` and `<version>` tags. The jar file contains the entire application.

When deploying to production this file should be placed along with an `application.properties` described above that the DevOps will set up.

Run the *packaged* version of the application. Type:

```bash
java -jar target/myapp-1.0.0-SNAPSHOT.jar
```

You will see same execution details in the log file.
