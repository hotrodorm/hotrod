# Hello World Ant

This guide shows how to generate the persistence layer using Apache Ant.

The Ant plugin included in HotRod works in Java 8 only. For newer versions of Java, use Maven instead

You'll need:

- Java.
- Apache Ant.
- A text editor. `Notepad` or `vi` will do, but you can use your favorite IDE if you prefer.
- No database installation necessary. We'll use a non-persistent in-memory H2 database in this example.

This guide sets up an Ant project, creates a table in the database, and generates the persistence from it.

**Note**: It's perfectly possible (albiet cumbersome) to build and run a Spring or Spring Boot application using Ant. This has been done for a few projects, although is heavily recommended to use Maven for its simplicity.

## 1. The build.xml File

In this part we create the Maven project, we lay out its structure, we and add all the necessary libraries and plugins to it.

If you are using a plain text editor (such as Notepad) you can create an empty folder and add the
files as described in the steps below. Alternatively, you can use your favorite IDE to create a
blank Ant or Java project.

Create the `build.xml` file as:

```xml
<project name="hello-world-ant" basedir="." default="gen">

  <target name="gen">
    <taskdef name="hotrodgen" classname="org.hotrod.plugin.ant.GenAntTask">
      <classpath >
        <fileset dir="lib">
          <include name="*.jar" />
        </fileset>
      </classpath>
    </taskdef>
    <hotrodgen 
        configfile="./layer.xml"
        jdbcdriverclass="org.h2.Driver"
        jdbcurl="jdbc:h2:mem:EXAMPLEDB;INIT=runscript from 'schema.sql';DB_CLOSE_DELAY=-1"
        jdbcusername="sa"
        jdbcpassword=""
        jdbcschema="PUBLIC"
    />
  </target>

</project>
```


Also, create the empty source folders, if they are not yet created. In linux you can do:

```bash
mkdir -p src/main/java/app
mkdir -p lib
```

Change the commands above accordingly for Windows or other OS as needed, or use your IDE to create them.

## 2. The Libraries

Download the HotRod Ant Plugin along with the JDBC driver of your database to the lib folder. In this example you can get them from Maven Central:

- [hotrod-ant-plugin-5.1.2-jar-with-dependencies.jar](https://repo1.maven.org/maven2/org/hotrodorm/hotrod/hotrod-ant-plugin/5.1.2/hotrod-ant-plugin-5.1.2-jar-with-dependencies.jar)
- [h2-2.2.224.jar](https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar)

## 3. The Database Tables

In this part we define the SQL script to create an in-memory table in H2 database.

Create the file `schema.sql` with the following SQL content:

```sql
create table account (
  id int primary key not null,
  name varchar(20)
);
```

## 4. The Persistence Layer Configuration

In this example the persistence layer will include a single table. Create the file `layer.xml` with the content:

```xml
<hotrod>

  <generators>
    <jdbc />
  </generators>

  <table name="account" />

</hotrod>
```

## 5. Generating the Persistence Layer

Now, let's use HotRod to generate the persistence code. Go to the project folder and type:

```bash
ant
```

We see the code generation details:

```bash
Buildfile: ./build.xml

gen:
[hotrodgen] HotRod Generator version 5.1.3 (build 20250920-165641) - Generate
[hotrodgen] 
[hotrodgen] Configuration File: ./layer.xml
[hotrodgen] Database URL: jdbc:h2:mem:EXAMPLEDB;INIT=runscript from 'schema.sql';DB_CLOSE_DELAY=-1
[hotrodgen] Database Name: H2 - version 2.1 (2.1.214 (2022-06-13))
[hotrodgen] JDBC Driver: H2 JDBC Driver - version 2.1 (2.1.214 (2022-06-13)) - implements JDBC Specification 4.2
[hotrodgen] HotRod Adapter: H2 Adapter
[hotrodgen]  
[hotrodgen] Current Schema: PUBLIC
[hotrodgen]  
[hotrodgen] Discover disabled.
[hotrodgen]  
[hotrodgen] Generating all facets.
[hotrodgen]  
[hotrodgen] Table ACCOUNT included.
[hotrodgen]  
[hotrodgen] Total of: 1 table, 0 views, 0 enums, 0 DAOs, and 0 sequences -- including 0 select methods, and 0 query methods.

BUILD SUCCESSFUL
Total time: 1 second
```

That's it! You just generated the persistence layer from the database to `src/main/java/app/persistence`. The DAOs, Layout, and Model classes are ready so you can use CRUD and LiveSQL features out of the box.

Later on, when the database suffers changes &mdash; it will &mdash; you can just
rerun the generation step `ant` to retrieve the latest changes to columns, tables, views, etc. and to apply
them automatically to the persistence layer.
