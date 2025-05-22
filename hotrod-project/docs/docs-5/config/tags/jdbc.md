# The `<jdbc>` Tag

This tag configures the details of the generated persistence layer. It defines the folder locations,
packages, and naming convention for the generated DAOs, Layout, and Model classes.

## Sub Tags

This configuration tag can optionally include any of the following tags, in the order shown below:

| Sub Tag | Description |
| -- | -- |
| `<dao>` | Configures the details of the generated DAO classes. DAOs provide all the persistence operations related to a table or view |
| `<layout>` | Configures the details of the generated Layout classes. A Layout class represents the structure of a table or view. It doesn't have any methods beyond the setters and getters |
| `<model>` | Configure the details of the generated Model classes. A model class represents the behavior of the domain object related to the table or view and it can be extended to add behavior or properties. This java class extends the Layout class |
| `<discover>` | Configure the auto-discovery of tables and views in one or more database schema |

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Specifies the base dir for all generated classes| `src/main/java` |
| `package`  | Specifies the base package for all generated classes | `app.persistence` |
| `qualifier` | Specifies the Spring qualifier to use in this persistence layer, to reference HotRod beans (such as LiveSQL beans) in case the application uses multiple data sources | *No Qualifier* |

## Default Configuration

If the `<jdbc>` tag is not specified &mdash; either due to the no-configuration mode in use or the intentional exclusion of it &mdash; the default configuration that is adopted corresponds to:

```xml
  <jdbc base-dir="src/main/java"
        package="app.persistence"
        qualifier="">
    <dao    prefix="" suffix="DAO"    base-dir="" subpackage="dao" />
    <layout prefix="" suffix="Layout" base-dir="" subpackage="layout" />
    <model  prefix="" suffix=""       base-dir="" subpackage="model" />
  </jdbc>
```


