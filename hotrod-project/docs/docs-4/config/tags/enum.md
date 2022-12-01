# The `<enum>` Tag

An `<enum>` tag tells the HotRod generator to include a static dimension table as a harcoded enum in the persistence layer. Each `<enum>` tag adds one table
to the persistence layer.

Enum tables are fully cached in the persistence layer of the application. This is suitable for dimension tables that do not change 
during the operation of the application, but can only change between new deployments of the application.

Enum tables should be used for dimension tables that have a limited number of rows. Otherwise the cached data will grow to the point 
where the resulting Java enum can take time to generate or can use a lot of memory.

When a foreign key related to the enum is found the persistence layer produces a the corresponding enum value instead of querying the
dimension table. This can bring performance benefits since queries read the fact tables and do not need to read the dimension table at all. Even, so 
all values of the dimensiion table are readily available in the persistence layer. Queries that relate enums can become much simpler 
and performant.


## Attributes

The `<enum>` tag include the following attributes:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the table | Required |
| `catalog` | The catalog of the table, if different from the default one | The current catalog, specified in the runtime properties file |
| `schema` | The schema of the table, if different from the default one | The current schema, specified in the runtime properties file |
| `java-name` | Specifies the name of the java enum | The table name in camel-case form |
| `name-column` | Specifies the column (a CHAR-like column) that will be used to produce the names of each enum value | Required |


## Included Tags

Apart from the rows read from the database table enums can also include hardcoded values specified in the configuration file using the 
`<non-persistent>` entries. The values from the database table and configuration entries are combined when generating the Java enum.

The `<non-persistent>` tag can have the following attributes:


| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The enum value. Must take the form of a Java upper case identifier | Required |
| `value` | A value for the enum entry | Required |

## Example

The following example generates a Java enum with the state_code and `id`:

```xml
<enum name="employee_state" name-column="state_code">
  <non-persistent value="100" name="NONE" />
  <non-persistent value="101" name="ALL" />
</enum>
```

If the table `employee_state` has 50 entries, the Java enum will end up having 52. It adds the two entries specified 
with the `<non-persistent>` tabs to the list of entries.



