# Creating a New Project from Scratch

To start a Maven project that uses HotRod's persistence you'll need to set up a typical Maven 
project for Spring or Spring Boot and then add the HotRod Generator Plugin and runtime libraries. This example
is for a Spring Boot project.

Then, you can create a table in a database and use the generator to generate the persistence code from it. With
the persistence code available, you can write your application and use all HotRod's features to simplify reading
and writing to the database.

**Note**: The example below uses a PostgreSQL database to create a table and then to use it. Change the JDBC driver according to the specific
database you are using.

## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

### Setting Up a Maven Project

Use your favorite tool to generate a blank Maven project or copy a basic pom.xml file from another project. Then:
- Add the Spring or Spring Boot dependency (the latter in this example) and the JDBC driver dependency according to your specific database.
- Then, add the HotRod and MyBatis Libraries. 
- Finally, add the HotRod Generator Plugin to it.

With these additions the `pom.xml` file should look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.myapp</groupId>
  <artifactId>main</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <springboot.version>2.3.4-RELEASE</springboot.version>
    <hotrod.version>3.4.7</hotrod.version>
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
          <localproperties>dev.properties</localproperties>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.5</version>
            <type>jar</type>
          </dependency>
        </dependencies>
      </plugin>

    </plugins>

  </build>

</project>
```

**Note**: Don't forget to change JDBC driver (in two places) according the database you are using.

Run Maven once, using `mvn clean` to make sure the `pom.xml` file is correctly formed.

## Part 2 &mdash; Create a Table and Generate the Persistence Code

In this section we create a table in the database, we generate the persistence code for Spring/Spring Boot. 

### Creating a Table in the Database

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

### Generating the Persistence Code

Tell HotRod how you want the generation to work. Create the file `hotrod.xml` (in the folder of
your choosing and with any name) and add:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod SYSTEM "hotrod.dtd">

<hotrod>

  <generators>
    <mybatis-spring>
      <daos package="com.myapp" 
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

Now, let's create the configuration file `dev.properties` (referenced by the `pom.xml`) with the database connection details. Create this file with the following content:

```properties
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

Now, let's use HotRod to generate the persistence code. Type:

`mvn hotrod:gen`

HotRod will connect to the database schema, will retrieve the table details, and will produce the code. It will create or update the following files:
* `src/main/java/com/myapp/primitives/EmployeeDAO.java`
* `src/main/java/com/myapp/primitives/EmployeeVO.java`
* `src/main/java/com/myapp/EmployeeImpl.java`
* `mappers/primitives-employee.xml`

**Note**: Since the `EmployeeImpl.java` may contain custom code, it's never overwritten. The other files are always overwritten to adhere with the
latest/current database structure.

At this ponit all the persistence code is ready to be used.

## Part 3 &mdash; Writing the Application and Running It

### Writing the Application

Let's write a simple application that perform two searches in the table we created before. Create the application class `src/main/java/com/myapp/app.java` as:

```java
package com.myapp;

import java.sql.SQLException;
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
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.myapp.persistence.primitives.EmployeeDAO;
import com.myapp.persistence.EmployeeVO;

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
    EmployeeVO e = this.employeeDAO.selectByPK(id);
    System.out.println("> Employee #" + id + " Name: " + e.getName());

    // Use LiveSQL to search for employees whose name starts with 'A'

    this.sql.
  }

}
```

### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and run both queries. We will the result:

```log
[ Starting example ]
> Employee #123 Name: Alice
[ Example complete ]
```
