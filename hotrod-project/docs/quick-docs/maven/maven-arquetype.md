# Maven Arquetype

HotRod includes a Maven arquetype that creates a full blown project using a single command line.

#### Table of Contents
1. [Create the Project Automatically](#create-the-project-automatically)
2. [Populate the Database](#populate-the-database)
3. [Generate the HotRod Persistence](#generate-the-hotrod-persistence)
4. [Run the Application](#run-the-application)
5. [Generate the OpenAPI JSON file](#generate-the-openapi-json-file)
6. [Generate the OpenAPI YAML file](#generate-the-openapi-yaml-file)
7. [Take a Database Structure Snapshot and Verify it Later - Optional](#take-a-database-structure-snapshot-and-verify-it-later)
8. [Parameters Reference](#parameters-reference)


#### Create the Project Automatically ####

You'll need to change the parameters according to:

- Your project details: Group, Artifact, Version, Packages.
- Library versions: SpringBoot, HotRod, MyBatis-Spring, Database Lifecycle, Sentinel.
- JDBC Driver details: artifact identification and driver class name.
- Sandbox database: URL, username, password, default catalog and/or schema.

**Note**: The Sandbox database is a database or schema that you use to develop the application and that you can 
populate and clean at any time without disrupting any other team member's work. It's a database schema for rapid development.

This is an example with Oracle database. Change parameters as needed for another database. See [parameters reference](#parameters-reference) at the end of this page for details.

```
mvn archetype:generate                                    \
  -DinteractiveMode=false                                 \
  -DarchetypeGroupId=org.hotrodorm.hotrod                 \
  -DarchetypeArtifactId=hotrod-archetype-sm-jar-app       \
  -DarchetypeVersion=3.4.5                                \
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
  -Ddbbuilderversion=1.2.2                                \
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

    cd app1

#### Populate the Database ####

Edit the files `src/main/database/1.0.0/build.sql` (and optionally `src/main/database/1.0.0/clean.sql`) to 
define a few tables that you want to create/drop with the DB lifecycle tasks. Then run:

```
mvn db:build
```

Now, your database has the table `employee` as defined by the `build.sql` file above. We are ready to run HotRod.

**Note**: As a side note, the commands to manage the database are:
- `mvn db:build`: to populate the database schema.
- `mvn db:clean`: to clean the database schema.
- `mvn db:rebuild`: to clean and build the database schema.

These DB lifecycle tasks are suitable for the sandbox environment only. You don't want to drop tables in the 
production environment, just by running `mvn db:clean`.

#### Generate the HotRod Persistence ####

Then, generate all classes and files for the HotRod persistence. Run:

    mvn hotrod:gen

This will generate all files, DAO, and VO Java classes necessary for the automated persistence.

#### Run the Application ####

Finally, fire up the ready-to-run application:

    mvn spring-boot:run

The application will start, will connect to the database, will retrieve employee #123, and will display its name:

```
[ Starting example ]
> Employee #123 Name: Alice
[ Example complete ]
```

You can call the REST services implemented in the example as. On a separate terminal use `wget` or `curl` to call some REST services:

```
wget -nv -O- localhost:8080/employee/123
{"id":123,"name":"Alice"}

wget -nv -O- localhost:8080/employee/search/Anne
[{"ID":45,"NAME":"Anne"}]
```

#### Generate the OpenAPI JSON file ####

To generate the OpenAPI JSON file type:

```
mvn -P genopenapijson clean verify
```

The JSON file is now available at `target/openapi.json`, ready for distribution.

**Note 1**: Make sure the application is not running while issuing this command. Maven will start and stop the service to retrieve the OpenAPI file.

**Note 2**: To generate the OpenAPI the `genopenapijson` profile needs to be enabled temporarily. It's disabled by default since you probably
don't want to generate the API on every single build.

#### Generate the OpenAPI YAML file ####

To generate the OpenAPI YAML file type:

```
mvn -P genopenapiyaml clean verify
```

The YAML file is now available at `target/openapi.yaml`, ready for distribution.

**Note 1**: Make sure the application is not running while issuing this command. Maven will start and stop the service to retrieve the OpenAPI file.

**Note 2**: To generate the OpenAPI the `genopenapiyaml` profile needs to be enabled temporarily. It's disabled by default since you probably
don't want to generate the API on every single build.
          

#### Take a Database Structure Snapshot and Verify it Later ####

This feature is disabled by default and can be removed altogether from the project.

This is an optional feature that can help validate a schema structure if you cannot access the environment. This can be useful if you can't get access
to the production environment, but you still want to make sure all tables, views, column, their definitions, and indexes are created and up to date in it.

If you want to enable the Sentinel database verifier (disabled by default) you must take a snapshot of the database structure when the database reaches a stable structure, by doing:

    mvn -P sentinel sentinel:take-snapshot@take
    
Then, enable the DatabaseVerifier in the App.java class to check it at runtime, and display differences in the log file.

Other Sentinel commands:

- `mvn -P sentinel sentinel:show-database@show-db`: displays the database structure of a live database.
- `mvn -P sentinel sentinel:take-snapshot@take`: takes a snapshot of the structure of a live database and saves it into a file.
- `mvn -P sentinel sentinel:show-snapshot@show-sn`: displays the content of a saved snapshot.
- `mvn -P sentinel sentinel:verify-database@verify`: verify the structure of a live database against a saved snapshot.
- `mvn -P sentinel sentinel:compare-snapshots@compare`: compares two saved snapshots for differences.

Notice that all of them include `-P sentinel` that enables the "sentinel" Maven profile. You don't want to run these commands by default on every single build.

#### Parameters Reference

The automated project creation requires parameters that fall into several categories:

##### Arquetype Selection

The arquetype selection tells Maven which specific template to use when creating a project:

- `archetypeArtifactId`: This is the specific arquetype to use. Currently the only arquetype available is `hotrod-archetype-sm-jar-app` that produces
a ready-to-run project with SpringBoot, REST services, HotRod, MyBatis, Database Lifecycle, Sentinel, and OpenAPI 3.
- `archetypeVersion`: The version of the maven arquetype. Choose at least `3.4.5`.

##### App Configuration

The app configuration specifies basic initial parameters for the new app:

- `groupId`: the groupid of your brand new app.
- `artifactId`: the artifactid of your brand new app.
- `version`: the version of your brand new app.
- `package`: the base package of the app classes.
- `persistencepackage`: the relative package of the persistence classes; typically you would choose something like `persistence`.

##### Libraries Selection

The librares selection section indicates the specific versions of each library you want to use in the brand new app:

- `springbootversion`: The Spring Boot version for your brand new project.
- `hotrodversion`: The HotRod version for your brand new project.
- `mybatisspringversion`: MyBatis-Spring version for your brand new project.
- `dbbuilderversion`: Automated database creation/lifecycle tool for your brand new project.
- `sentinelversion`: Database verifier tool for your brand new project.

##### JDBC Driver Selection

This section defines the specific JDBC driver to use. These values reference the Maven Central Repository. Search for groupid, artifactid, and version in the official repositories:

- `jdbcdrivergroupid`: the JDBC driver groupid.
- `jdbcdriverartifactid`: the the JDBC driver artifactid.
- `jdbcdriverversion`: The JDBC driver version.
- `jdbcdrivertype`: (Optional) The JDBC driver classifier. Defaults to `jar`.
- `jdbcdriverclassname`: The JDBC driver's driver class name.

##### Sandbox Database Selection

This section identifies the database sandbox that you will use to develop, debug, and run the application. 
HotRod retrieves the database structure from it.
This is a database or schema that you use to develop the application and that you can 
populate and clean at any time without disrupting any other team member's work. 
It's a database schema for rapid development:

- `jdbcurl`: The JDBC URL for the sandbox database.
- `jdbcusername`: The JDBC username for the sandbox database.
- `jdbcpassword`: The JDBC password for the sandbox database.
- `jdbccatalog`: The JDBC catalog (if supported) for the sandbox database.
- `jdbcschema`: The JDBC schema (if supported) for the sandbox database.

Typically databases support a schema, or a catalog, or both. As a general reference, see table below:

```
Database          Supports Catalog  Supports Schema 
----------------  ----------------  --------------- 
Oracle            --                Yes             
DB2               --                Yes             
PostgreSQL        --                Yes             
SQL Server        Yes               Yes             
MariaDB           Yes               --              
MySQL             Yes               --              
SAP ASE (Sybase)  Yes               Yes             
H2                --                Yes             
HyperSQL          --                Yes             
Apache Derby      --                Yes             
```















