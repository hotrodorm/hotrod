# Generating the Persistence Code with HotRod

The `gen` goal of hotRod encompasses the main functionality of this ORM.

To use this goal a few prerequisites need to be ready:
- A sandbox database with the latest database structure changes needs to be ready and accessible.
- The connection details to this database need to be available, including the JDBC URL and credentials.
- The HotRod Maven plugin needs to be configured in the `pom.xml`.
- The HotRod Configuration file needs to be prepared to tailor the persistence to our needs.

Once these prerequisites are prepared you can run the HotRod generator by typing:

```bash
mvn hotrod:gen
```

This Maven goal will inspect the database, and will generate the persistence code according to the specifications.

The guide to [Set up a Maven project with Spring Boot](../hello-world/creating-a-new-project.md) describes all the steps
to set up a project manually, using a text editor and Maven.

It's also possible to automate the project creation with a [Maven Arquetype](./maven-arquetype.md).

See [The HotRod Configuration](../config/configuration-file-structure.md) file for a comprehensive reference on the configuration details.



