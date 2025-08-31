# The `<model>` Tag

This tag configures the generation of Model classes.

The persistence layer includes one Model class for each table and view in the persistence layer, either declared in the configuration file or discovered by the discovery rule.

It will also include one Model class for each `<select>` tag in an executor `<dao>` tag (the one along with the tables) to add behavior to the data rows coming from the execution of the corresponding SELECT query.

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Overrides the base dir specified in the parent `<jdbc>` tag | *parent base-dir* |
| `package`  | Overrides the package specified in the parent `<jdbc>` tag  | *parent package* |
| `sub-package`  | Specifies the extra sub-package for Model classes | `model` |
| `prefix` | Specifies the prefix to prepend to the Model class names | *empty* |
| `suffix` | Specifies the suffix to append to the Model class names | `Model` |


