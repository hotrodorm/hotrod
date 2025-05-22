# The `<dao>` Tag

This tag configures the generation of DAO classes.

The persistence layer includes one DAO class for each table and view in the persistence layer, either declared in the configuration file or discovered by the discovery rule.

It will also include one DAO for each `<dao>` executor tag (the one along with the tables) to assemble all its queries.

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Overrides the base dir specified in the `<jdbc>` tag for the generated DAO classes | value in the `<jdbc>` tag |
| `subpackage`  | Specifies the extra sub-package for DAO classes | `dao` |
| `prefix` | Specifies the prefix to prepend to the DAO class names | *empty* |
| `suffix` | Specifies the suffix to append to the DAO class names | `DAO` |


