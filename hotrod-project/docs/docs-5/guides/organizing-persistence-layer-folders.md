# Organizing the Persistence Layer Folders

HotRod can organize the folder of the persistence layer to accommodate different solutions.

This can come in handy when the application uses multiple databases or schemas that you prefer to keep separate, or when you want to have full control of the folder structure, maybe to mimic a folder organization used in another application.


## Default Settings

For example, when using the default settings with a database schema that has two tables (EMPLOYEE and COMPANY) the generated persistence layer takes the form:

```
<PROJECT_HOME>
  + src/main/java/
    + app/persistence/
      + LayerConfigurationBean.java
      + dao/
        + EmployeeDAO.java
        + CompanyDAO.java
      + layout/
        + EmployeeLayout.java
        + CompanyLayout.java
      + model/
        + Employee.java
        + Company.java
```

All folders are relative to the `<PROJECT_HOME>` folder. In the example:

- `src/main/java` is the base directory of the source code.
- `app.persistence` is the default base package for the persistence layer, and this is translated to the `app/persistence` folder.
- `dao` is the default sub-package for the DAO classes.
- `layout` is the default sub-package for the Layout classes.
- `model` is the default sub-package for the Model classes.
- `MainLayerConfiguration` includes the persistence layer runtime rules.

The default settings are used in the no-config mode &ndash; when the hotrod XML configuration file is not specified &ndash; or when this files includes an empty `<jdbc/>` tag, with not declared settings.


## Default Configuration

An empty `<jdbc/>` tag is equivalent to:

```xml
  <jdbc base-dir="src/main/java"
        package="app.persistence">
    <dao    sub-package="dao"    prefix="" suffix="DAO" />
    <layout sub-package="layout" prefix="" suffix="Layout" />
    <model  sub-package="model"  prefix="" suffix="" />
  </jdbc>
```

See details at [The &lt;jdbc/> tag](../config/tags/jdbc.md).

When the `<dao>`, `<layout>`, and `<model>` define a `base-dir` or a `package` these ones override the default ones defined in the parent `<jdbc>` tag. Changing them allows each type of classes to be placed in fully separated folder structures.


## Multiple Databases or DataSources

If your application manages two or more databases and/or datasources it's a best practice to place their persistence layers in fully separate folders, so they don't interfere with each other.

The simplest solution is to use a different base `package` values for each one. In other cases they could be stored in fully separated java class path locations or separated Maven modules by specifying different values for `base-dir` in one (or both) of them.

For example, if we want to use the default package for the first database and the package `app.persistence2` for the second database we need two HotRod XML files, one per database:

- In the first HotRod XML file we can use the default settings in the `<jdbc/>` tag, as in:
```xml
    <jdbc base-dir="src/main/java"
          package="app.persistence"
          qualifier="Main">
      <dao    sub-package="dao"    prefix="" suffix="DAO" />
      <layout sub-package="layout" prefix="" suffix="Layout" />
      <model  sub-package="model"  prefix="" suffix="" />
    </jdbc>
```
- While the second HotRod XML file can define a different base package:
```xml
    <jdbc base-dir="src/main/java"
          package="app.persistence2"
          qualifier="Accounting">
      <dao    sub-package="dao"    prefix="" suffix="DAO" />
      <layout sub-package="layout" prefix="" suffix="Layout" />
      <model  sub-package="model"  prefix="" suffix="" />
    </jdbc>
```

If the first database has the tables COMPANY and EMPLOYEE, while the second one has the tables ACCOUNT and BRANCH the persistence layers will reside side by side, as in:

```
<PROJECT_HOME>
  + src/main/java/
    + app/persistence/
      + LayerConfigurationBean.java
      + dao/
        + CompanyDAO.java
        + EmployeeDAO.java
      + layout/
        + CompanyLayout.java
        + EmployeeLayout.java
      + model/
        + Company.java
        + Employee.java
    + app/persistence2/
      + LayerConfigurationBean.java
      + dao/
        + AccountDAO.java
        + BranchDAO.java
      + layout/
        + AccountLayout.java
        + BranchLayout.java
      + model/
        + Account.java
        + Branch.java
```

Alternatively, if we wanted the second persistence layer in the fully separated base dir `src/database2/java` we could use the following settings in the second HotRod XML file:
```xml
    <jdbc base-dir="src/database2/java"
          package="app.persistence"
          qualifier="Accounting">
      <dao    sub-package="dao"    prefix="" suffix="DAO" />
      <layout sub-package="layout" prefix="" suffix="Layout" />
      <model  sub-package="model"  prefix="" suffix="" />
    </jdbc>
```

In this case the persistence layers will be places in separate base dirs, as in:

```
<PROJECT_HOME>
  + src/main/java/
    + app/persistence/
      + LayerConfigurationBean.java
      + dao/
        + CompanyDAO.java
        + EmployeeDAO.java
      + layout/
        + CompanyLayout.java
        + EmployeeLayout.java
      + model/
        + Company.java
        + Employee.java
  + src/database/java/
    + app/persistence/
      + LayerConfigurationBean.java
      + dao/
        + AccountDAO.java
        + BranchDAO.java
      + layout/
        + AccountLayout.java
        + BranchLayout.java
      + model/
        + Account.java
        + Branch.java
```

### The Qualifier

When multiple databases or data sources are used in an application, there will be one `LiveSQL` and `LayerConfiguration` bean per layer. To distinguish them, you'll need to define a `qualifier` for each one, so your applications and beans can use the appropriate ones.

In the example above, the first persistence layer defines the qualifier `Main`, while the second persistence layer uses the qualifier `Accounting`.

When using LiveSQL, for example, the application can pick the correct one by using these qualifiers, as in:

```java
  @Autowired
  @Qualifier("dataSource:main")
  private LiveSQL sql1;

  @Autowired
  @Qualifier("dataSource:accounting")
  private LiveSQL sql2;
```

It's important to use the correct LiveSQL instance on each query to address the correct tables, and the correct SQL dialect, should these database be of different brands, editions, or versions.









