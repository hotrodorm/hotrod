# Libraries Dependencies

HotRod uses a different set of libraries at runtime and when generating the persistence code.

## At Runtime

HotRod has a single dependency at runtime:

```xml
  <dependency>
    <groupId>org.hotrodorm.hotrod</groupId>
    <artifactId>hotrod-livesql</artifactId>
    <version>5.0.0</version>
  </dependency>
```

The `hotrod-livesql-<version>.jar` library includes all classes nneded to develop and run the functionality of the persistence layer. all functionality for the LiveSQL functionality. It's required to run HotRod.

## Generating Persistence Code

When generating the persistence code Maven uses the HotRod Maven Plugin. See [Maven Integration](../maven/README.md) for details and [Hello World](../guides/hello-world.md) for a simple example.

