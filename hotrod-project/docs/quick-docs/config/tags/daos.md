# The `<daos>` Tag

This tag configures the generation of Java class files. This includes the DAO classes themselves and 
all the VOs (values objects) generated to hold data being retrieved from and sent to the database.

The MyBatis-Spring generator will produce on DAO for:

- Each `<table>` tag in the configuration file:
  - The DAO includes a method for each CRUD operation on the table.
  - The DAO also includes one method for each `<query>`, `<select>`, and `<sequence>` tag included in it.
- Each `<view>` tag in the configuration file:
  - The DAO includes a method for each CRUD operations on the view, when allowed by the database engine and view.
  - The DAO also includes one method for each `<query>`, `<select>`, and `<sequence>` tag included in it.
- Each `<dao>` tag in the configuration file. The DAO includes one method for each `<query>`, `<select>`, and `<sequence>` tag included in it.

Also, it will produce one VO for:

- Each `<table>` tag in the configuration file. The VO represents a row of the table.
- Each `<view>` tag in the configuration file. The VO represents a row of the view.
- Each `<select>` tag in the configuration file:
  - For Flat selects it will produce one VO.
  - For Structure selects it can produce zero, one, or multiple VOs, depending on its configuration.

The attributes of the `<daos>` tag are described in the following table:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `base-dir` | The base dir of the Java source folder | `src/main/java` |
| `package` | The package to use to place the VOs, inside the base dir | N/A |
| `primitives-package` | The relative package to create primitive classes for DAOs and parent VOs | `primitives` |
| `dao-prefix` | The prefix to add to the DAO names | (empty string) |
| `dao-suffix` | The suffix to add to the DAO names | `DAO` |
| `vo-prefix` | The prefix to add to the VO names | (empty string) |
| `vo-suffix` | The suffix to add to the VO names | `VO` |
| `abstract-vo-prefix` | The prefix to add to the parent VO names | (empty string) |
| `abstract-vo-suffix` | The suffix to add to the parent VO names | (empty string) |
| `ndao-prefix` | Nitro module: The prefix to add to the DAO names | (empty string) |
| `ndao-suffix` | Nitro module: The suffix to add to the DAO names | (empty string) |
| `nvo-prefix` | Nitro module: The prefix to add to the VO names | (empty string) |
| `nvo-suffix` | Nitro module: The suffix to add to the VO names | (empty string) |
| `nabstract-vo-prefix` | Nitro module: The prefix to add to the parent VO names | (empty string) |
| `nabstract-vo-suffix` | Nitro module: The suffix to add to the parent VO names | (empty string) |
| `primitives-prefix` | *Deprecated* | N/A |
| `primitives-suffix` | *Deprecated* | N/A |



  
