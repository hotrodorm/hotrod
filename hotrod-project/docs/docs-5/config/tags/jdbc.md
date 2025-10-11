# The `<jdbc>` Tag

This tag configures the details of the generated persistence layer. It defines the folder locations,
packages, and naming convention for the generated DAOs, Layout, and Model classes.

See [Organizing the Persistence Layer](../../guides/organizing-persistence-layer-folders.md) for examples.

## Sub Tags

This configuration tag can optionally include any of the following tags, in the order shown below:

| Sub Tag | Description |
| -- | -- |
| [&lt;discover>](./discover.md) | Configure the auto-discovery of tables and views in one or more database schema |
| [&lt;layer-resources>](./jdbc-layer-resources.md) | Configures the details of the generated layer resources bean. This bean keeps the runtime properties related to the persistence layer |
| [&lt;dao>](./jdbc-dao.md) | Configures the details of the generated DAO classes. DAOs provide all the persistence operations related to a table or view |
| [&lt;layout>](./jdbc-layout.md) | Configures the details of the generated Layout classes. A Layout class represents the structure of a table or view. It doesn't have any methods beyond the setters and getters |
| [&lt;model>](./jdbc-model.md) | Configure the details of the generated Model classes. A model class represents the behavior of the domain object related to the table or view and it can be extended to add behavior or properties. This java class extends the Layout class |

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Specifies the base dir for all generated classes| `src/main/java` |
| `package`  | Specifies the base package for all generated classes | `app.persistence` |
| `qualifier` | Specifies the Spring qualifier to use in the persistence layer, to reference beans (such as dataSource and LiveSQL beans) when the application uses multiple data sources. Use a different qualifier for each dataSource. Don't specify it if the application has a single persistence layer. See [Using Multiple DataSources](../../guides/using-multiple-datasources.md) for examples on how to use it | *none* |

## Default Configuration

If the `<jdbc>` tag is not specified &mdash; either due to the no-configuration mode in use or the intentional exclusion of it &mdash; the default configuration that would be used could correspond to:

```xml
  <jdbc base-dir="src/main/java"
        package="app.persistence">
    <dao    sub-package="dao"    prefix="" suffix="DAO" />
    <layout sub-package="layout" prefix="" suffix="Layout" />
    <model  sub-package="model"  prefix="" suffix="" />
  </jdbc>
```

