# Using Multiple Datasources

This guide shows how to configure two datasources that use a HotRod's generated persistence.

This strategy can be used to use any number of static datasources; that is, the list of datasources
is known when building the applicatio. Each datasource is configured independently and can be
defined for the same database engine or different ones, as needed.

Using Spring beans it's also possible to register and use a dynamic list of datasources. This can
be useful in special cases where the number of databases vary at runtime. The dynamic creation,
registration, and use of dynamic beans is out of the scope of this guide.

For a quick guide on how to set up a simple project with one datasource only see
[Hello World](./hello-world.md).

This guide will use two separate H2 databases, each one having a different table.


## Part 1 &mdash; Setting Up the Project

First we create the Maven project, we lay out its structure, we and add all the necessary libraries
and plugins to it.

### Setting Up the Maven Project

The Maven project will define two profiles, one for each database's code generation, that are inactive by default. This example uses two H2 databases (with different tables) but it can accommodate any number of databases of the same or different types.

The `pom.xml` file will look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>twodbs</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <hotrod.version>4.0.0</hotrod.version>
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
      <version>${hotrod.version}</version>
    </dependency>

    <dependency> <!-- Required. HotRod's LiveSQL library -->
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>${hotrod.version}</version>
    </dependency>

    <dependency> <!-- Required. The generator uses MyBatis for database connectivity -->
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>2.1.3</version>
    </dependency>

    <dependency> <!-- The database(s)' JDBC drivers. In this example both databases use the same driver -->
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

    </plugins>

  </build>

  <profiles>

    <profile>
      <id>first</id> <!-- Only the table "account" exists here -->
      <build>
        <plugins>
          <plugin>
            <groupId>org.hotrodorm.hotrod</groupId>
            <artifactId>hotrod-maven-plugin</artifactId>
            <version>${hotrod.version}</version>
            <configuration>
              <configfile>./first.xml</configfile>
              <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
              <jdbcurl>jdbc:h2:mem:FIRSTDB;INIT=runscript from './first.sql';DB_CLOSE_DELAY=-1</jdbcurl>
              <jdbcusername>sa</jdbcusername>
              <jdbcpassword>""</jdbcpassword>
              <jdbccatalog>FIRSTDB</jdbccatalog>
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
    </profile>

    <profile>
      <id>second</id> <!-- Only the table "invoice" exists here -->
      <build>
        <plugins>
          <plugin>
            <groupId>org.hotrodorm.hotrod</groupId>
            <artifactId>hotrod-maven-plugin</artifactId>
            <version>${hotrod.version}</version>
            <configuration>
              <configfile>./second.xml</configfile>
              <jdbcdriverclass>org.h2.Driver</jdbcdriverclass>
              <jdbcurl>jdbc:h2:mem:SECONDDB;INIT=runscript from './second.sql';DB_CLOSE_DELAY=-1</jdbcurl>
              <jdbcusername>sa</jdbcusername>
              <jdbcpassword>""</jdbcpassword>
              <jdbccatalog>SECONDDB</jdbccatalog>
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
    </profile>

  </profiles>

</project>
```

Create the empty source folders, if they are not yet created. In linux you can do (change accordingly for other OSes):

```bash
mkdir -p src/main/java
mkdir -p src/main/resources
```

To check the `pom.xml` file is correct, run Maven once using:

```bash
mvn clean compile
```

It should report `BUILD SUCCESS` at the end.


## Part 2 &mdash; Creating the Schemas and Generating Both Persistence Layers

In this part we create two in-memory schemas in H2 and we generate both persistence layers from them.


### Preparing the Database Scripts

For the first database create the file `first.sql` with the following SQL content:

```sql
drop table if exists account;

create table account (
  id int primary key not null,
  owner varchar(50),
  balance int
);

insert into account (id, owner, balance) values (1, 'Tom', 500);
insert into account (id, owner, balance) values (2, 'Kim', 450);
insert into account (id, owner, balance) values (3, 'Sue', 830);
```

Then, for the second database create the file `second.sql` with the following SQL content:

```sql
drop table if exists invoice;

create table invoice (
  id int primary key not null,
  client varchar(50),
  amount int
);

insert into invoice (id, client, amount) values (101, 'Acme', 78);
insert into invoice (id, client, amount) values (102, 'Indus', 1680);
insert into invoice (id, client, amount) values (103, 'Lotus Inc', 450);
```


### Preparing the HotRod Configuration Files

Tell HotRod how you want the generation to work for each database. Create the file
`first.xml` for the first database:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod SYSTEM "hotrod.dtd">

<hotrod>

  <generators>
    <mybatis-spring>
      <daos package="com.app.first.daos"
        dao-suffix="DAO" vo-suffix="Impl" abstract-vo-prefix="" abstract-vo-suffix="VO"
        sql-session-bean-qualifier="sqlSession1" live-sql-dialect-bean-qualifier="liveSQLDialect1" />
      <mappers dir="mappers/first" />
    </mybatis-spring>
  </generators>

  <table name="account" />

</hotrod>
```

Then, create the file `second.xml` for the second database:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod SYSTEM "hotrod.dtd">

<hotrod>

  <generators>
    <mybatis-spring>
      <daos package="com.app.second.daos"
        dao-suffix="DAO" vo-suffix="Impl" abstract-vo-prefix="" abstract-vo-suffix="VO"
        sql-session-bean-qualifier="sqlSession2" live-sql-dialect-bean-qualifier="liveSQLDialect2" />
      <mappers dir="mappers/second" />
    </mybatis-spring>
  </generators>

  <table name="invoice" />

</hotrod>
```

Notice that the `sql-session-bean-qualifier` and `live-sql-dialect-bean-qualifier` properties are different in the persistence layers. That allows each persistence layer to use the correct datasource.


### Generating the Persistence Layers

Now, let's use HotRod to generate the persistence layers.

Generate the persistence layer for the first database. Type:

```bash
mvn -P first hotrod:gen
```

Generate the persistence layer for the second database. Type:

```bash
mvn -P second hotrod:gen
```

Each time HotRod connected to the correct database schema, retrieved the table details, and produced the persistence layer.


## Part 3 &mdash; The Application

In this part we write a simple app that uses the CRUD and LiveSQL functionalities from each database.


### A Simple Spring Boot Application

Let's write a simple application that performs two searches in each database. Create the application
class `src/main/java/com/app/App.java` as:

```java
package com.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.app.first.daos.primitives.AccountDAO;
import com.app.first.daos.primitives.AccountDAO.AccountTable;
import com.app.first.daos.primitives.AccountVO;
import com.app.first.daos.AccountImpl;

import com.app.second.daos.primitives.InvoiceDAO;
import com.app.second.daos.primitives.InvoiceDAO.InvoiceTable;
import com.app.second.daos.primitives.InvoiceVO;
import com.app.second.daos.InvoiceImpl;

@Configuration
@SpringBootApplication
@ComponentScan
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  @Qualifier("liveSQL1") // bean name defined in DataSourceConfig1.java
  private LiveSQL sql1;

  @Autowired
  @Qualifier("liveSQL2") // bean name defined in DataSourceConfig2.java
  private LiveSQL sql2;

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

    // Use a DAO to search in datasouce #1
    {
      AccountVO a = this.accountDAO.selectByPK(2);
      System.out.println("Account #2: " + a);
    }

    // Use a DAO to search in datasouce #2
    {
      InvoiceVO i = this.invoiceDAO.selectByPK(103);
      System.out.println("Invoice #103: " + i);
    }

    // Use LiveSQL to search in datasource #1
    {
      AccountTable a = AccountDAO.newTable();
      List<Row> rows = this.sql1.select().from(a).where(a.owner.like("%m%")).execute();
      System.out.println("Account which owner names' have an 'm':");
      for (Row r : rows) {
        System.out.println(r);
      }
    }

    // Use LiveSQL to search in datasource #2
    {
      InvoiceTable i = InvoiceDAO.newTable();
      List<Row> rows = this.sql2.select().from(i).where(i.amount.ge(300)).execute();
      System.out.println("Invoices for more than $300:");
      for (Row r : rows) {
        System.out.println(r);
      }
    }

  }

}
```

Using two or more datasources require to configure multiple beans of the same class. The following two classes do this job for each database.

Add the class `src/main/java/com/app/DataSource1Config.java`:

```java
package com.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSource1Config {

  @Bean
  @Primary
  @ConfigurationProperties("datasource1")
  public HikariDataSource dataSource1() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Value("${datasource1.mappers}")
  private String mappers;

  @Bean
  public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource dataSource1) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource1);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
        .getResources(this.mappers + "/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory1);
    return sqlSession;
  }

  @Value("${datasource1.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource1.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource1.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource1.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource1.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect1(@Qualifier("dataSource1") DataSource dataSource1,
      @Qualifier("sqlSession1") SqlSessionTemplate sqlSession1) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource1, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    return liveSQLDialect;
  }

  @Bean
  public MapperFactoryBean<LiveSQLMapper> liveSQLMapper1(
      @Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1) throws Exception {
    MapperFactoryBean<LiveSQLMapper> factoryBean = new MapperFactoryBean<>(LiveSQLMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory1);
    return factoryBean;
  }

  @Bean
  public LiveSQL liveSQL1(@Qualifier("sqlSession1") SqlSessionTemplate sqlSession1, //
      @Qualifier("liveSQLDialect1") LiveSQLDialect liveSQLDialect1, //
      @Qualifier("liveSQLMapper1") LiveSQLMapper liveSQLMapper1 //
  ) throws Exception {
    LiveSQL ls = new LiveSQL(sqlSession1, liveSQLDialect1, liveSQLMapper1);
    return ls;
  }

}
```

Add the class `src/main/java/com/app/DataSource2Config.java`:

```java
package com.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSource2Config {

  @Bean
  @ConfigurationProperties("datasource2")
  public HikariDataSource dataSource2() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Value("${datasource2.mappers}")
  private String mappers;

  @Bean
  public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource2) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource2);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
        .getResources(this.mappers + "/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory2);
    return sqlSession;
  }

  @Value("${datasource2.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource2.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource2.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource2.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource2.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect2(@Qualifier("dataSource2") DataSource dataSource2,
      @Qualifier("sqlSession2") SqlSessionTemplate sqlSession2) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource2, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    return liveSQLDialect;
  }

  @Bean
  public MapperFactoryBean<LiveSQLMapper> liveSQLMapper2(
      @Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2) throws Exception {
    MapperFactoryBean<LiveSQLMapper> factoryBean = new MapperFactoryBean<>(LiveSQLMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory2);
    return factoryBean;
  }

  @Bean
  public LiveSQL liveSQL2(@Qualifier("sqlSession2") SqlSessionTemplate sqlSession2, //
      @Qualifier("liveSQLDialect2") LiveSQLDialect liveSQLDialect2, //
      @Qualifier("liveSQLMapper2") LiveSQLMapper liveSQLMapper2 //
  ) throws Exception {
    LiveSQL ls = new LiveSQL(sqlSession2, liveSQLDialect2, liveSQLMapper2);
    return ls;
  }

}
```


### Prepare the Runtime Properties File

The runtime properties provide details of each database for the running the application. Create the file `application.properties` as:

```properties
# General configuration of the app

logging.level.root=INFO

# First datasource configuration

datasource1.driver-class-name=org.h2.Driver
datasource1.jdbc-url=jdbc:h2:mem:FIRSTDB;INIT=runscript from './first.sql';DB_CLOSE_DELAY=-1
datasource1.username=sa
datasource1.password=
datasource1.mappers=mappers/first

#datasource1.livesqldialectname=MYSQL
#datasource1.livesqldialectdatabaseName=MariaDB
#datasource1.livesqldialectversionString="10.6"
#datasource1.livesqldialectmajorVersion=10
#datasource1.livesqldialectminorVersion=6

# Second datasouce configuration

datasource2.driver-class-name=org.h2.Driver
datasource2.jdbc-url=jdbc:h2:mem:SECONDDB;INIT=runscript from './second.sql';DB_CLOSE_DELAY=-1
datasource2.username=sa
datasource2.password=
datasource2.mappers=mappers/second

#datasource2.livesqldialectname=POSTGRESQL
#datasource2.livesqldialectdatabaseName="PostgreSQL 12"
#datasource2.livesqldialectversionString="12.4 (Linux)"
#datasource2.livesqldialectmajorVersion=12
#datasource2.livesqldialectminorVersion=4

```


### Run the Application

Now, let's run the application. Type:

```bash
mvn spring-boot:run
```

The Spring Boot application starts, connects to the database and run both queries. We see the result shown below:

```log
[ Starting example ]
Account #2: com.app.first.daos.AccountImpl@1f66d8e1
- id=2
- owner=Kim
- balance=450
Invoice #103: com.app.second.daos.InvoiceImpl@721d5b74
- id=103
- client=Lotus Inc
- amount=450
Account which owner names' have an 'm':
{OWNER=Tom, ID=1, BALANCE=500}
{OWNER=Kim, ID=2, BALANCE=450}
Invoices for more than $300:
{AMOUNT=1680, ID=102, CLIENT=Indus}
{AMOUNT=450, ID=103, CLIENT=Lotus Inc}
[ Example complete ]
```

That's it! You just built and run a Spring Boot app with two datasources, each one with
its own persistence layer.

