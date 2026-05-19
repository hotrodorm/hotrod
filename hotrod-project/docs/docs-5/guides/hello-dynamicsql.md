# Hello Dynamic SQL

This app shows examples for all DynamicSQL tags that are included in Nitro.

DynamicSQL allows you to run SQL queries with static or iterative sections that are included or excluded depending on the runtime parameters in each execution.

To run this example you'll need:

- Java installed
- Maven installed
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example

After following all the steps of this guide our main project folder will include the files and folders shown below:

```bash
application.properties
pom.xml
layer.xml
schema.sql
src/main/java
```

## Part 1 &mdash; Setting Up the Project

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

Create the `pom.xml` file as:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>examples</groupId>
  <artifactId>hello-dynamicsql</artifactId>
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
      <version>5.1.23</version>
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
        <version>5.1.23</version>
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

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating a Table and Defining the Queries

In this part we define the tables that will reside in the H2 database. We also define the custom Nitro queries that we want to run in it.

### Prepare the Database Script that Creates the Database

Create the file `schema.sql` with the following SQL content:

```sql
create table employee (
  id int primary key not null,
  first_name varchar(20),
  last_name varchar(20),
  hired_on date,
  active char(1),
  city_id int,
  title varchar(30)  
);

insert into employee (id, first_name, last_name, hired_on, active, city_id, title) values
  (101, 'Peter', 'Smith', '2026-01-12', 'A', 14, 'Medic'),
  (105, 'Marie', 'Jonas', '2021-02-15', 'P', 14, 'Virtual Assistant'),
  (223, 'Anne', 'Smith', '2021-02-15', 'A', 14, 'Secretary'),
  (518, 'Arabella', 'Lawson', '2021-02-15', 'A', 27, 'Virtual Assistant'),
  (640, 'Lucy', 'Barr', '2021-02-15', 'P', 27, 'Clerk'),
  (1077, 'Lucas', 'Santon', '2021-02-15', 'P', 27, 'Virtual Assistant'),
  (1241, 'Peter', 'Arunsen', '2025-09-17', 'A', 30, 'Office Manager');
```

### Define the Nitro Queries

Create the `layer.xml` file with:

```xml
<hotrod>

  <generators>
    <jdbc />
  </generators>

  <table name="employee">

    <select method="search1">
      <parameter name="f" type="app.Search1Filter" />
      SELECT *
      FROM employee
      WHERE active = 'A'
      <if test="f.filterActive">
        <if test="f.firstName != null"> AND first_name = #{f.firstName}</if>
        <if test="f.lastName != null"> AND last_name = #{f.lastName}</if>
      </if>
    </select>

    <select method="search2">
      <parameter name="ordering" type="Integer" />
      SELECT *
      FROM employee
      WHERE active = 'A'
      <complement>
        <choose>
          <when test="ordering == 1"> ORDER BY first_name</when>
          <when test="ordering == 2"> ORDER BY last_name</when>
          <when test="ordering == 3"> ORDER BY hired_on DESC</when>
          <otherwise> ORDER BY city_id</otherwise>
        </choose>
      </complement>
    </select>

    <select method="search3">
      <parameter name="ids" type="Integer[]" />
      SELECT *
      FROM employee
      <complement>
        WHERE id IN
        <foreach item="id" collection="ids" open="(" separator=", " close=")">
          #{id}
        </foreach>
      </complement>
    </select>

    <select method="search4">
      <parameter name="filter" type="java.util.Map&lt;Integer, String[]>" />
      SELECT *
      FROM employee
      <complement>
        WHERE active = 'A'
        <foreach item="entry" collection="filter.entrySet()" open="AND (" separator="OR" close=")">
          city_id = #{entry.key} AND title IN
          <foreach item="title" collection="entry.value" open="(" separator=", " close=")">
            #{title}
          </foreach>
        </foreach>
      </complement>
    </select>

    <select method="search5">
      <parameter name="partialName" type="String" />
      <parameter name="config" type="app.AppConfiguration" />
      <bind name="pattern" value="'%' + partialName + '%'" />
      <bind name="plan" value="config.plans['basicPlan']" />
      SELECT *
      FROM employee
      WHERE last_name LIKE #{pattern}
      <choose>
        <when test="plan.code == 1">AND city_id = 1</when>
        <when test="plan.title != null">AND lower(title) = #{plan.title.toLowerCase()}</when>
        <when test="plan.vip">AND city_id = 30</when>
      </choose>
      <if test="plan.ordered == 'Y'">ORDER BY hired_on DESC</if>
    </select>

    <select method="search6">
      <parameter name="fn" type="Boolean" />
      <parameter name="ln" type="Boolean" />
      <parameter name="hd" type="Boolean" />
      SELECT id
      <trim>
        <if test="fn">, first_name</if>
        <if test="ln">, last_name</if>
        <if test="hd">, hired_on</if>
      </trim>
      FROM employee
    </select>

    <select method="search7">
      <parameter name="first" type="String" />
      <parameter name="last" type="String" />
      <parameter name="hiredDate" type="java.time.LocalDate" />
      SELECT *
      FROM employee
      <where separator="OR">
        <if test="first != null">first_name = #{first}</if>
        <if test="last != null">last_name = #{last}</if>
        <if test="hiredDate != null">hired_on > #{hiredDate}</if>
      </where>
    </select>

    <query method="update1">
      <parameter name="active" type="String" />
      <parameter name="hiredOn" type="java.time.LocalDate" />
      <parameter name="cityId" type="Integer" />
      UPDATE employee
      <set>
        <if test="active != null">active = #{active}</if>
        <if test="hiredOn != null">hired_on = #{hiredOn}</if>
      </set>
      WHERE city_id = #{cityId}
    </query>
  </table>

</hotrod>
```

### Generate the Persistence Layer

Now, let's generate the persistence layer. Type:

```bash
mvn hotrod:gen
```

The layer generation reports:

```bash
[INFO] HotRod Generator version 5.1.23 (build 20250920-205252) - Generate
[INFO] 
[INFO] Configuration File: ./layer.xml
[INFO] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
[INFO] Database Name: H2 - version 2.2 (2.2.224 (2022-06-13))
[INFO] JDBC Driver: H2 JDBC Driver - version 2.2 (2.2.224 (2022-06-13)) - implements JDBC Specification 4.2
[INFO] HotRod Adapter: H2 Adapter
[INFO]  
[INFO] Current Schema: PUBLIC
[INFO]  
[INFO] Discover disabled.
[INFO]  
[INFO] Generating all facets.
[INFO]  
[INFO] Table EMPLOYEE included.
[INFO]  - Query update1 included.
[INFO]  - Select search1 included.
[INFO]  - Select search2 included.
[INFO]  - Select search3 included.
[INFO]  - Select search4 included.
[INFO]  - Select search5 included.
[INFO]  - Select search6 included.
[INFO]  - Select search7 included.
[INFO]  
[INFO] Total of: 1 table, 0 views, 0 enums, 0 DAOs, and 0 sequences -- including 7 select methods,
and 1 query method.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

HotRod connected to the database schema, discovered the details of the table and queries, and generated the persistence layer.

All generated classes are placed inside `src/main/java/app/persistence`.


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities to read from the database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in the table. Create the application 
class `src/main/java/app/App.java` as:

```java
package app;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.AppConfiguration.Plan;
import app.persistence.dao.EmployeeDAO;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private EmployeeDAO testDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      example1();
      example2();
      example3();
      example4();
      example5();
      example6();
      example7();
      example8();
    };
  }

  private void example1() {
    System.out.println("Example 1 - Using <if>: Assembling a dynamic search predicate");
    Search1Filter filter = new Search1Filter();
    filter.setFilterActive(true);
    filter.setLastName("Smith");
    List<Employee> rows = this.testDAO.search1(filter);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example2() {
    System.out.println("Example 2 - Using <choose>: Select different ordering at runtime");
    List<Employee> rows = this.testDAO.search2(2);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example3() {
    System.out.println("Example 3 - Simple <foreach>: Iterate over an array");
    Integer[] ids = { 170, 223, 640 };
    List<Employee> rows = this.testDAO.search3(ids);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example4() {
    System.out.println("Example 4 - Advanced <foreach>: Assemble complex predicates by nesting <foreach>");
    Map<Integer, String[]> filter = new HashMap<>();
    filter.put(14, new String[] { "Nurse", "Medic", "Engineer" });
    filter.put(27, new String[] { "Virtual Assistant" });
    List<Employee> rows = this.testDAO.search4(filter);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example5() {
    System.out.println("Example 5 - Using <bind>: Using variables to simplify expressions and property navigation");

    String partialName = "mi";

    AppConfiguration config = new AppConfiguration();
    Map<String, Plan> plans = new HashMap<>();
    plans.put("basicPlan", Plan.of(null, "Medic", true, true));
    config.setPlans(plans);

    List<Employee> rows = this.testDAO.search5(partialName, config);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example6() {
    System.out.println("Example 6 - Using <trim>: Assemble dynamic lists of segments using separators");
    List<Employee> rows = this.testDAO.search6(true, false, true);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example7() {
    System.out.println("Example 7 - Using <where>: Assemble a dynamic WHERE predicate");
    List<Employee> rows = this.testDAO.search7("Peter", null, LocalDate.of(2025, 6, 12));
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example8() {
    System.out.println("Example 8 - Using <set>: Assemble a dynamic SET clause in an UPDATE query");
    String newStatus = "A";
    Integer cityId = 27;
    LocalDate hiredOn = LocalDate.of(2026, 9, 13);
    int count = this.testDAO.update1(newStatus, hiredOn, cityId);
    System.out.println("* Update count=" + count);
  }

}
```

Let's add the class `src/main/java/app/AppConfiguration.java` as:

```java
package app;

import java.util.Map;

public class AppConfiguration {

  private Map<String, Plan> plans;

  public final Map<String, Plan> getPlans() {
    return plans;
  }

  public final void setPlans(Map<String, Plan> plans) {
    this.plans = plans;
  }

  public static class Plan {

    private Integer code;
    private String title;
    private boolean vip;
    private boolean ordered;

    private Plan(Integer code, String title, boolean vip, boolean ordered) {
      this.code = code;
      this.title = title;
      this.ordered = ordered;
      this.vip = vip;
    }

    public static Plan of(Integer code, String title, boolean vip, boolean ordered) {
      return new Plan(code, title, vip, ordered);
    }

    public final Integer getCode() {
      return code;
    }

    public final String getTitle() {
      return title;
    }

    public final boolean isVip() {
      return vip;
    }

    public final boolean isOrdered() {
      return ordered;
    }

  }

}
```

Let's also add the class `src/main/java/app/Search1Filter.java` as:

```java
package app;

public class Search1Filter {

  private boolean filterActive;
  private String firstName;
  private String lastName;

  public final boolean isFilterActive() {
    return filterActive;
  }

  public final void setFilterActive(boolean filterActive) {
    this.filterActive = filterActive;
  }

  public final String getFirstName() {
    return firstName;
  }

  public final void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public final String getLastName() {
    return lastName;
  }

  public final void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
```

### The Runtime Properties File

The runtime properties are used when running the application. Create the file `application.properties` as:

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

logging.level.app.persistence.dao=TRACE
```


### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and runs both queries. Let's review the log file step by step:

#### Example 1 - Using <if>: Assembling a dynamic search predicate
```log
SQL:
      SELECT *
      FROM employee
      WHERE active = 'A'
         AND last_name = ?

JDBC Parameters (1):
  1. f.lastName: Smith (java.lang.String)

* found 2 employees: id=101, id=223
```

#### Example 2 - Using <choose>: Select different ordering at runtime

```log
SQL:
      SELECT *
      FROM employee
      WHERE active = 'A'
         ORDER BY last_name

JDBC Parameters (0):
  N/A

* found 4 employees: id=1241, id=518, id=101, id=223
```

#### Example 3 - Simple <foreach>: Iterate over an array

```log
SQL:
      SELECT *
      FROM employee
        WHERE id IN
        (
          ?
        , 
          ?
        , 
          ?
        )

JDBC Parameters (3):
  1. id#0: 170 (java.lang.Integer)
  2. id#1: 223 (java.lang.Integer)
  3. id#2: 640 (java.lang.Integer)

* found 2 employees: id=223, id=640
```

#### Example 4 - Advanced <foreach>: Assemble complex predicates by nesting <foreach>

```log
SQL:
      SELECT *
      FROM employee
        WHERE active = 'A'
        AND (
          city_id = ? AND title IN
          (
            ?
          )
        OR
          city_id = ? AND title IN
          (
            ?
          , 
            ?
          , 
            ?
          )
        )

JDBC Parameters (6):
  1. entry.key#0: 27 (java.lang.Integer)
  2. title#0: Virtual Assistant (java.lang.String)
  3. entry.key#1: 14 (java.lang.Integer)
  4. title#1: Nurse (java.lang.String)
  5. title#2: Medic (java.lang.String)
  6. title#3: Engineer (java.lang.String)

* found 2 employees: id=101, id=518
```

#### Example 5 - Using <bind>: Using variables to simplify expressions and property navigation

```log
SQL:
      SELECT *
      FROM employee
      WHERE last_name LIKE ?
      AND lower(title) = ?
      ORDER BY hired_on DESC

JDBC Parameters (2):
  1. pattern: %mi% (java.lang.String)
  2. plan.title.toLowerCase(): medic (java.lang.String)

* found 1 employees: id=101
```

#### Example 6 - Using <trim>: Assemble dynamic lists of segments using separators

```log
SQL:
      SELECT id
      , first_name, hired_on
      FROM employee

JDBC Parameters (0):
  N/A

* found 7 employees: id=101, id=105, id=223, id=518, id=640, id=1077, id=1241
```

#### Example 7 - Using <where>: Assemble a dynamic WHERE predicate

```log
SQL:
      SELECT *
      FROM employee
      WHERE first_name = ?
  OR hired_on > ?

JDBC Parameters (2):
  1. first: Peter (java.lang.String)
  2. hiredDate: 2025-06-12 (java.time.LocalDate)

* found 2 employees: id=101, id=1241
```

#### Example 8 - Using <set>: Assemble a dynamic SET clause in an UPDATE query

```log
SQL:
      UPDATE employee
SET active = ?,
    hired_on = ?
      WHERE city_id = ?

JDBC Parameters (3):
  1. active: A (java.lang.String)
  2. hiredOn: 2026-09-13 (java.time.LocalDate)
  3. cityId: 27 (java.lang.Integer)

* update count=3
```

Later on, if the database suffers changes that affect your Nitro queries you can just
rerun the generation step `mvn hotrod:gen` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
