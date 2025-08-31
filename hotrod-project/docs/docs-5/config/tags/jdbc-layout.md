# The `<layout>` Tag

This tag configures the generation of Layout classes.

The persistence layer includes one Layout class for each table and view in the persistence layer, either declared in the configuration file or discovered by the discovery rule.

It will also include one Layout class for each `<select>` tag in an executor `<dao>` tag (the one along with the tables) to accomodate all data coming from the execution of the corresponding SELECT query.

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Overrides the base dir specified in the parent `<jdbc>` tag | *parent base-dir* |
| `package`  | Overrides the package specified in the parent `<jdbc>` tag  | *parent package* |
| `sub-package`  | Specifies the extra sub-package for Layout classes | `layout` |
| `prefix` | Specifies the prefix to prepend to the Layout class names | *empty* |
| `suffix` | Specifies the suffix to append to the Layout class names | `Layout` |


