# Libraries Dependencies

HotRod uses a different set of libraries at runtime and when generating the persistence code.

## At Runtime

HotRod requires the following libraries at runtime:

- The `hotrod-<version>.jar` library of the specific version you want to use.
- A `hotrod-livesql-<version>.jar` library for the LiveSQL functionality.
- A MyBatis library `mybatis-spring-boot-starter-<version>.jar` that implements the underlying layer of persistence.

### The HotRod Library

This library includes all infrastructure for the CRUD functionality. It's required to run HotRod.

### The LiveSQL Library

This library includes all functionality for the LiveSQL functionality. It's required to run HotRod.

### The MyBatis Library

Currently, the only supported Generator is for MyBatis persistence. Therefore, this library is also required.

## Generating Persistence Code

When generating the persistence code only one library is required. This comes packaged in the corresponding plugin, so 
its details are embedded there. See:

- [Maven Integration](../maven/maven.md#maven-plugin).
- [Ant Integration](../ant/ant.md#ant-plugin).
- `Eclipse Integration`.

## Version Features 

For a list of version changes and added features see [Version History](../version-history.md).
