# Maven

HotRod includes Maven support since version 3.

First, HotRod is available as a dependency at the official Maven repository and its mirrors.

In order to generate persistence code from Maven, HotRod includes a Maven plugin that implements several Maven goals with the main aim to generate persistence code. The main goal is `gen`, while other supporting Maven goals assist complex generation logic, such as sophisticated type solving or name solving.

Finally, HotRod includes a Maven Arquetype that can create a full running Spring Boot project in one command line.

## Maven Dependencies

All HotRod modules can be found as dependencies in the Maven Central Repository at [https://search.maven.org/search?q=g:org.hotrodorm.hotrod](org.hotrodorm.hotrod).

A Spring or Spring Boot project needs to declare two main libraries and a third one for MyBatis support. The typical dependency declaration in a `pom.xml` file  takes the form:

```xml
    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod</artifactId>
      <version>3.4.7</version>
    </dependency>

    <dependency>
      <groupId>org.hotrodorm.hotrod</groupId>
      <artifactId>hotrod-livesql</artifactId>
      <version>3.4.7</version>
    </dependency>

    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>2.1.3</version>
    </dependency>  
```

## Maven Plugin

The Maven Plugin implements four Maven goals. Namely:
- `gen` to generate persistence code.
- ``

To use the Maven Plugin declare it in the `pom.xml` file under the section `<projects><build><plugins>`. In its simplest
form the plugin can look like:

```xml
      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>3.4.7</version>
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
```

In this declaration the tags `<groupId>`, `<artifactId>`, and `<version>` define the HotRod Maven Plugin and its specific version.
Then the `<configuration>` tags includes a series of optional configuration parameters for this plugin. Finally, the `<dependencies>`
tag can be used to specify the JDBC library (or any other library) that the plugin requires.

### Configuration Properties

The Maven Plugin requires a few configuration parameters to operate. First, it needs the specific connection details to the sandbox 
database where the tables can be found, as well as the default schema in it. In addition to this, it also requires a couple of
extra configuration parameters to tailor the code generation.

To configure the Maven Plugin we can set its parameters in two ways.

First, we can add one tag per parameter inside the `<configuration>` tag. The example above includes the parameter `<localproperties>`
explicitly defined. The full list of parameters is:

| Parameter | Description |
|---|---|
| localproperties | Defines an external properties file that will be read to load these properties |
| configfile | Specifies the HotRod Main configuration file location. This is the main configuration file that lays out all the details of the generation such as packages, folders, naming conventions, and converters are defined, as well as the full list tables and Nitro queries. |
| generator | The generator to use. Currently only one generator is supported: `MyBatis-Spring`  |
| jdbcdriverclass | The JDBC driver class name |
| jdbcurl | The JDBC connection URL |
| jdbcusername | The JDBC connection username |
| jdbcpassword | The JDBC connection password |
| jdbccatalog | The JDBC catalog of the schema (optional) |
| jdbcschema | The JDBC schema name (optional) |
| facets | Comma-separated list of facets to generate. Leave empty to activate them all |
| display | The console display type. Valid values are `list` and `summary`. Defaults to `list`.  |

The first parameter `<localproperties>` is an optional parameter that can be used to extract all the parameters from the `pom.xml` file into a separate
file of our choosing. This can be handy if we want to avoid placing database credentials in the main `pom.xml` file. If omitted, parameters can be set up 
in the `pom.xml`. 

Parameters in the external local file defined in the `<localproperties>` tag can coexist with parameters directly included in the `pom.xml` file. In case 
a parameter is defined in both places, the one in the properties file supersedes the one in the `pom.xml`.

#### Examples

All configuration is done in the `pom.xml` file. The `<localproperties>` tag is omitted:

`pom.xml`:

```xml
      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>3.4.7</version>
        <configuration>
          <configfile>src/main/database/hotrod.xml</configfile>
          <generator>MyBatis-Spring</generator>
          <jdbcdriverclass></jdbcdriverclass>
          <jdbcdriverclass>org.postgresql.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:postgresql://192.168.56.214:5432/mydatabase</jdbcurl>
          <jdbcusername>myusername</jdbcusername>
          <jdbcpassword>mypassword</jdbcpassword>
          <jdbccatalog></jdbccatalog>
          <jdbcschema>public</jdbcschema>
          <facets></facets>
          <display>list</display>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.5</version>
          </dependency>
        </dependencies>
      </plugin>
```

All configuration is done in the properties file. Only the `<localproperties>` tag is included:

**`pom.xml`**:

```xml
      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>3.4.7</version>
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
```

`hotrod.properties`:

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

The configuration is fully (or partially) defined in the `pom.xml` and some values from the properties file supersedes the `pom.xml` file.

`pom.xml`:

```xml
      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>3.4.7</version>
        <configuration>
          <configfile>src/main/database/hotrod.xml</configfile>
          <generator>MyBatis-Spring</generator>
          <jdbcdriverclass></jdbcdriverclass>
          <jdbcdriverclass>org.postgresql.Driver</jdbcdriverclass>
          <jdbcurl>jdbc:postgresql://192.168.56.214:5432/mydatabase</jdbcurl>
          <jdbccatalog></jdbccatalog>
          <jdbcschema>public</jdbcschema>
          <facets></facets>
          <display>list</display>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.5</version>
          </dependency>
        </dependencies>
      </plugin>
```

`hotrod.properties`:

```properties
jdbcurl=jdbc:postgresql://192.168.56.214:5432/mydatabase
jdbcusername=myusername
jdbcpassword=mypassword
```
