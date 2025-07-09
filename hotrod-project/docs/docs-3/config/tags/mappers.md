# The `<mappers>` Tag

This tag configures the location of the generated MyBatis mapper files.

It has the following attributes:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `base-dir` | The base dir where to generate the mapper files | `src/main/resources` |
| `dir` | The relative dir (relative to the base dir) where to generate the mapper files | N/A and required |
| `namespace` | The base namespace for the mappers | `mappers` |

## The namespace

The namespace is used for identifying SQL executions in CRUD methods.

For example, to enable DEBUG level of logging for CRUD operations related to the EMPLOYEE table add the following line to the `application.properties` file:

```properties
logging.level.mappers.employee=DEBUG
```

To enable logging for a single CRUD method related to the EMPLOYEE table, you can do:

```properties
logging.level.mappers.employee.selectByPK=DEBUG
```


