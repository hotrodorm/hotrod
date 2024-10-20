# Maven Arquetype

HotRod includes a Maven arquetype that creates a full blown project using a single command line. 

The Maven arquetype automates the creation of a project that includes:
- Full Maven structure.
- Fully configured HotRod for the database of your choice.
- Configurable versions of HotRod and other libraries.
- Database populated by Debbie (optional).
- Database monitored by Sentinel (optional).
- A REST service.
- OpenAPI generation in JSON format for an API Gateway.
- OpenAPI generation in YAML format for an API Gateway.
- Runnable basic application with example code.

### Table of Contents

- [Create the Project](#create-the-project-with-one-command)
- [Populate the Database](#populate-the-database)
- [Generate the HotRod Persistence](#generate-the-hotrod-persistence)
- [Run the Application](#run-the-application)
- [What To Do Next](#what-to-do-next)
    - [Generate the OpenAPI JSON file](#generate-the-openapi-json-file)
    - [Generate the OpenAPI YAML file](#generate-the-openapi-yaml-file)
    - [Restore the Database Baseline with Debbie](#restore-the-database-baseline-with-debbie)
    - [Verify the Database Structure with Sentinel](#verify-the-database-structure-with-sentinel)
- [Parameters Reference](#parameters-reference) 

## Create the Project with One Command

You'll need to change the parameters according to:

- **The Project Details**: Group, Artifact, Version, Packages.
- **Library Versions**: SpringBoot, HotRod, MyBatis-Spring, Debbie (optional), Sentinel (optional).
- **JDBC Driver Details**: artifact identification and driver class name.
- **Sandbox Database**: URL, username, password, default catalog and/or schema.

**Note**: The Sandbox database is a database or schema that you use to develop the application and that you can 
populate and clean at any time without disrupting any other team member's work. It's a database schema for rapid development.

This is an example with Oracle database. Change parameters as needed to switch to a different database engine and to
point to your local database database. See [parameters reference](#parameters-reference) at the end of this page for more details.

```bash
mvn archetype:generate                                    \
  -DinteractiveMode=false                                 \
  -DarchetypeGroupId=org.hotrodorm.hotrod                 \
  -DarchetypeArtifactId=hotrod-archetype-sm-jar-app       \
  -DarchetypeVersion=3.4.6                                \
  \
  -DgroupId=com.app1                                      \
  -DartifactId=app1                                       \
  -Dversion=1.0.0-SNAPSHOT                                \
  -Dpackage=com.myapp                                     \
  -Dpersistencepackage=persistence                        \
  \
  -Dspringbootversion=2.3.4.RELEASE                       \
  -Dhotrodversion=3.4.5                                   \
  -Dmybatisspringversion=2.1.3                            \
  -Ddebbieversion=1.3.2                                   \
  -Dsentinelversion=1.1.8                                 \
  \
  -Djdbcdrivergroupid=com.oracle.ojdbc                    \
  -Djdbcdriverartifactid=ojdbc8                           \
  -Djdbcdriverversion=19.3.0.0                            \
  -Djdbcdriverclassname=oracle.jdbc.driver.OracleDriver   \
  \
  -Djdbcurl="jdbc:oracle:thin:@192.168.56.95:1521:orcl"   \
  -Djdbcusername=user1                                    \
  -Djdbcpassword=pass1                                    \
  -Djdbccatalog=""                                        \
  -Djdbcschema=USER1
```

Now, you can enter the newly created directory `app1` (that corresponds to the `artifactid` parameter above):

```bash
cd app1
```

## Populate the Database

Connect to the sandbox database to create a table and add a few rows. Run the following queries:

```sql
create table employee (
  id number(9) primary key not null, 
  name varchar2(20)
);

insert into employee (id, name) values (45, 'Anne');
insert into employee (id, name) values (123, 'Alice');
insert into employee (id, name) values (6097, 'Steve');
```

Now, your database has the table `employee`. We are ready to run HotRod.

## Generate the HotRod Persistence

Then, generate all classes and files for the HotRod persistence. Run:

```bash
mvn hotrod:gen
```

This will generate all files, DAO, and VO Java classes necessary for the automated persistence.

## Run the Application

Finally, fire up the ready-to-run application:

```bash
mvn spring-boot:run
```

The application will start, will connect to the database, will retrieve employee #123, and will display their name:

```bash
[ Starting example ]
> Employee #123 Name: Alice
[ Example complete ]
```

You can call the REST services implemented in the example as. On a separate terminal use `wget` or `curl` to call some REST services:

```bash
wget -nv -O- localhost:8080/employee/123
{"id":123,"name":"Alice"}

wget -nv -O- localhost:8080/employee/search/Anne
[{"ID":45,"NAME":"Anne"}]
```

## What To Do Next

### Generate the OpenAPI JSON file

To generate the OpenAPI JSON file type:

```bash
mvn -P genopenapijson clean verify
```

The JSON file is now available at `target/openapi.json`, ready for distribution.

**Note**: Make sure the application is not running while issuing this command. Maven will start and stop the service to retrieve the OpenAPI file.


### Generate the OpenAPI YAML file

To generate the OpenAPI YAML file type:

```bash
mvn -P genopenapiyaml clean verify
```

The YAML file is now available at `target/openapi.yaml`, ready for distribution.

**Note**: Make sure the application is not running while issuing this command. Maven will start and stop the service to retrieve the OpenAPI file.
  

### Restore the Database Baseline with Debbie

Instead of connecting to the database and run SQL statements one by one, you can use Debbie to run them for you.

Edit the files `src/main/database/1.0.0/build.sql` (and optionally `src/main/database/1.0.0/clean.sql`) to 
define a few tables that you want to create/drop with the DB lifecycle tasks. Then run:

```bash
mvn debbie:build
```

Now, your database has the table `employee` as defined by the `build.sql` file above.

See [Debbie Project](https://github.com/justtheimpaler/debbie#readme) for more details on how to use it. 
In a nutshell, the commands to manage the database are:

- `mvn debbie:build`: to populate the database schema.
- `mvn debbie:clean`: to clean the database schema.
- `mvn debbie:rebuild`: to clean and build the database schema in one go.

Debbie tasks are suitable for the sandbox environment only. You don't want to drop tables in the 
production environment, because you ran `mvn debbie:clean` by mistake.
          

### Verify the Database Structure with Sentinel

Sentinel can help validating a schema structure from the command line or at runtime in your SpringBoot application. 
This can be useful if you can't get access to the production environment but you still want to make sure all tables, 
views, column, their definitions, and indexes in this live database are created and are up to date in it.

To use Sentinel you need to take a database baseline snapshot first and then compare it with the target one (that typically
is the live database).

Take a snapshot of the sandbox database structure:

```bash
mvn -P sentinel sentinel:take-snapshot@take
```
    
Then, to check the schema structure at runtime, uncomment the `DatabaseVerifier` method call in the `App.java` class. 
At runtime this will search for and display any differences in the log file.

Other Sentinel commands:

- `mvn -P sentinel sentinel:show-database@show-db`: displays the database structure of a live database.
- `mvn -P sentinel sentinel:take-snapshot@take`: takes a snapshot of the structure of a live database and saves it into a file.
- `mvn -P sentinel sentinel:show-snapshot@show-sn`: displays the content of a saved snapshot.
- `mvn -P sentinel sentinel:verify-database@verify`: verify the structure of a live database against a saved snapshot.
- `mvn -P sentinel sentinel:compare-snapshots@compare`: compares two saved snapshots for differences.

Sentinel can help finding unnoticed differences between different environments (dev, test and prod databases). Also, if developers have a myriad of local
databases it can also help pinpointing differences between them.

**Note**: The Oracle database is particularly slow at producing schema metadata. You'll notice the Sentinel can take a minute or more connecting to it. 
If this is slowing down your app start up cycle too much, you can comment out Sentinel. Alternatively you can perform the validation less often -- maybe only once a day.


## Parameters Reference

The automated project creation requires parameters that fall into several categories:

### Arquetype Selection

The arquetype selection tells Maven which specific template to use when creating a project:

- `archetypeArtifactId`: This is the specific arquetype to use. Currently the only arquetype available is `hotrod-archetype-sm-jar-app` that produces
a ready-to-run project with SpringBoot, REST services, HotRod, MyBatis, Debbie, Sentinel, and OpenAPI 3.
- `archetypeVersion`: The version of the maven arquetype. Choose at least `3.4.6`.

### App Configuration

The app configuration specifies basic initial parameters for your new app:

- `groupId`: the groupid of your brand new app.
- `artifactId`: the artifactid of your brand new app.
- `version`: the version of your brand new app.
- `package`: the base package of the app classes.
- `persistencepackage`: the relative package of the persistence classes; typically you would choose something like `persistence`.

### Libraries Selection

The librares selection section indicates the specific versions of each library you want to use in the brand new app:

- `springbootversion`: The Spring Boot version for your brand new project.
- `hotrodversion`: The HotRod version for your brand new project.
- `mybatisspringversion`: MyBatis-Spring version for your brand new project.
- `debbieversion`: Version of Debbie (automated database preparation). Leave blank to exclude Debbie from the project.
- `sentinelversion`: Version of Sentinel (database structure verifier). Leave blank to exclude Sentinel from the project.

### JDBC Driver Selection

This section defines the specific JDBC driver to use. These values reference the Maven Central Repository. Search for groupid, artifactid, and version in the official repositories:

- `jdbcdrivergroupid`: the JDBC driver groupid.
- `jdbcdriverartifactid`: the the JDBC driver artifactid.
- `jdbcdriverversion`: The JDBC driver version.
- `jdbcdrivertype`: (Optional) The JDBC driver classifier. Defaults to `jar`.
- `jdbcdriverclassname`: The JDBC driver's driver class name.

For a quick list of JDBC drivers available as a Maven dependency see [JDBC Drivers Examples](jdbc-drivers-examples.md).

### Sandbox Database Selection

This section identifies the database sandbox that you will use to develop, debug, and run the application. 
HotRod retrieves the database structure from it.
This is a database or schema that you use to develop the application and that you can 
populate and clean at any time without disrupting any other team member's work. 
It's a database schema for rapid development:

- `jdbcurl`: The JDBC URL for the sandbox database.
- `jdbcusername`: The JDBC username for the sandbox database.
- `jdbcpassword`: The JDBC password for the sandbox database.
- `jdbccatalog`: The JDBC catalog in the sandbox database. Leave blank if not supported.
- `jdbcschema`: The JDBC schema in the sandbox database. Leave blank if not supported.

Typically databases support a schema, a catalog, or both. For details see [JDBC Catalogs &amp; Schemas](jdbc-catalogs-and-schemas.md).



