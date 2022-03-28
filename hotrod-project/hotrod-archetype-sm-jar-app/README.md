#### Create the Project Automatically ####

HotRod includes a Maven arquetype that creates a full blown project using a single command line.
You'll need to change the parameters according to:

- Your project details: Group, Artifact, Version, Packages.
- Library versions: SpringBoot, HotRod, MyBatis-Spring, Database Builder, Sentinel.
- Specific database: JDBC Driver details, URL, username, password, default catalog/schema.


This is an example with Oracle database. Change parameters as needed for another database.

```
mvn archetype:generate                                    \
  -DinteractiveMode=false                                 \
  -DarchetypeGroupId=org.hotrodorm.hotrod                 \
  -DarchetypeArtifactId=hotrod-archetype-sm-jar-app       \
  -DarchetypeVersion=3.4.5-SNAPSHOT                       \
  \
  -DgroupId=com.app1                                      \
  -DartifactId=app1                                       \
  -Dversion=1.0.0-SNAPSHOT                                \
  -Dpackage=com.myapp                                     \
  -Dpersistencepackage=persistence                        \
  \
  -Dspringbootversion=2.3.4.RELEASE                       \
  -Dhotrodversion=3.4.5-SNAPSHOT                          \
  -Dmybatisspringversion=2.1.3                            \
  -Ddbbuilderversion=1.2.2                                \
  -Dsentinelversion=1.1.8                                 \
  \
  -Djdbcdrivergroupid=com.oracle.ojdbc                    \
  -Djdbcdriverartifactid=ojdbc8                           \
  -Djdbcdriverversion=19.3.0.0                            \
  -Djdbcdriverclassname=oracle.jdbc.driver.OracleDriver   \
  -Djdbcurl="jdbc:oracle:thin:@192.168.56.95:1521:orcl"   \
  -Djdbcusername=user1                                    \
  -Djdbcpassword=pass1                                    \
  -Djdbccatalog=""                                        \
  -Djdbcschema=USER1                                      \
```

Now, you can enter the newly created directory with the name of the artifact:

    cd app1

#### Populate the Database ####

Edit the files `src/main/database/1.0.0/build.sql` and `src/main/database/1.0.0/clean.sql` to 
define the tables you want to create automatically. Then run:

- `mvn db:build` to populate the database schema.
- `mvn db:clean` to clean the database schema.
- `mvn db:rebuild` to clean and build the database schema.

#### Generate the HotRod Persistence ####

Run:

    mvn hotrod:gen

This will generate all necessary DAO and VO Java classes, with fully typed and fully named properties.

#### Run the Application ####

    mvn spring-boot:run

The application will start, will connect to the database, will retrieve employee #123, and will display its name:

```
[ Starting example ]
> Employee #123 Name: Alice
[ Example complete ]
```

#### Take Sentinel Database Snapshot ####

    mvn -P sentinel sentinel:take-snapshot@take
    
Then, enable the DatabaseVerifier in the App.java class to check it at runtime.

Other Sentinel commands:

```
mvn -P sentinel -pl persistence sentinel:show-database@show-db
mvn -P sentinel -pl persistence sentinel:take-snapshot@take
mvn -P sentinel -pl persistence sentinel:show-snapshot@show-sn
mvn -P sentinel -pl persistence sentinel:verify-database@verify
mvn -P sentinel -pl persistence sentinel:compare-snapshots@compare    
```

