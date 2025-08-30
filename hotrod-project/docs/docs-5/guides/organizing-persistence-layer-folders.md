# Organizing the Persistence Layer Folders

HotRod can organize the folder of the persistence layer to accommodate different solutions.

This can come in handy when the application uses multiple databases or schemas that you prefer to keep separate, or when you want to have full control of the folder structure, maybe to mimic a folder organization used in another application.

## Default Settings

For example, when using the default settings with a database schema that has two tables (EMPLOYEE and COMPANY) the generated persistence layer takes the form:

```
<PROJECT_HOME>
  + src/main/java/
    + app/persistence/
      + MainLayerConfiguration.java
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
- `MainLayerConfiguration` holds the persistence layer rules.

The default settings are used in the no-config mode &ndash; when the hotrod XML configuration file is not specified &ndash; or when this files includes an empty `<jdbc/>` tag, with not declared settings.

## Default Configuration

An empty `<jdbc/>` tag is equivalent to:

```xml
  <jdbc base-dir="src/main/java"
        package="app.persistence"
        qualifier="">
    <dao    base-dir="" package="" subpackage="dao"    prefix="" suffix="DAO" />
    <layout base-dir="" package="" subpackage="layout" prefix="" suffix="Layout" />
    <model  base-dir="" package="" subpackage="model"  prefix="" suffix="" />
  </jdbc>
```

See details at [The &lt;jdbc/> tag](../config/tags/jdbc.md).

When the `<dao>`, `<layout>`, and `<model>` define a `base-dir` or a `package` these ones override the default ones defined in the parent `<jdbc>` tag. Changing them allows each type of classes to be placed in fully separated folder structures.

## Multiple Databases or DataSources

If your application manages two or more databases and/or datasources it's a best practice to place their persistence layers in fully separate folders, so they don't interfere with each other.

The simplest solution is to use a different base `package` values for each one. In other cases they could be stored in fully separated java class path locations or separated Maven modules by specifying different values for `base-dir` in one (or both) of them.

For example, if we want to use the default package for the first database and the package `app.persistence2` for the second database we need two HotRod XML files, one per database:

1. In the first HotRod XML file we can use the default setting in the `<jdbc/>` tag:
```xml
    <jdbc base-dir="src/main/java"
          package="app.persistence"
          qualifier="">
      <dao    base-dir="" package="" subpackage="dao"    prefix="" suffix="DAO" />
      <layout base-dir="" package="" subpackage="layout" prefix="" suffix="Layout" />
      <model  base-dir="" package="" subpackage="model"  prefix="" suffix="" />
    </jdbc>
```
2. The second HotRod XML file can look like:
```xml
      <jdbc base-dir="src/main/java"
            package="app.persistence"
            qualifier="">
        <dao    base-dir="" package="" subpackage="dao"    prefix="" suffix="DAO" />
        <layout base-dir="" package="" subpackage="layout" prefix="" suffix="Layout" />
        <model  base-dir="" package="" subpackage="model"  prefix="" suffix="" />
      </jdbc>
```
3. Test
```xml
        <jdbc base-dir="src/main/java"
              package="app.persistence"
              qualifier="">
          <dao    base-dir="" package="" subpackage="dao"    prefix="" suffix="DAO" />
          <layout base-dir="" package="" subpackage="layout" prefix="" suffix="Layout" />
          <model  base-dir="" package="" subpackage="model"  prefix="" suffix="" />
        </jdbc>
```


## The Default Folder Structure

If your schema has two tables (EMPLOYEE and COMPANY) then the persistence layer generated with the default organization will look like:

```
/my/project/dir/
  + code/base/dir/
    + persistence/layer/base/package/
      + LayerConfiguration.java
      + model/
        + Employee.java
        + Company.java
      + layout/
        + EmployeeLayout.java
        + CompanyLayout.java
      + daos/
        + EmployeeDAO.java
        + CompanyDAO.java
```



By default the persistence layer of your application is organized in an straigforward folder structure that holds the DAO classes, the layout classes, and the model classes in separate folders.
With the out-of-the box configuration, the persistence layer is generated 

