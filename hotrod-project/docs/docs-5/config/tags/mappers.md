# The `<mappers>` Tag

This is an optional tag used to configure the location of the generated MyBatis mapper files.

It has the following attributes:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `base-dir` | The base dir where to generate the mapper files | `src/main/resources` |
| `dir` | The relative dir (relative to the base dir) where to generate the mapper files | `mappers` |
| `namespace` | The base namespace for the mappers | `mappers` |

If this tag is omitted the mappers are generated at `src/main/resources/mappers`.


## The namespace

The namespace is used for identifying SQL executions in CRUD methods for logging purposes. 

The namespace is also useful to differentiate mappers when multiple datasources are in use. This way, the CRUD mappers for
each datasource can be fully differentiated from another datasource, even if they happen to have similar tables or views.

For example, to enable DEBUG level of logging for CRUD operations related to the EMPLOYEE table add the following line to the `application.properties` file:

```properties
logging.level.mappers.employee=DEBUG
```

To enable logging for a single CRUD method related to the EMPLOYEE table, you can do:

```properties
logging.level.mappers.employee.selectByPK=DEBUG
```


