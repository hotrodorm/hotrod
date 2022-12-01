# The `<hotrod-fragment>` Tag

This is the root tag of a configuration file fragment.

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `package` | The Java base package where all the persistence layer will be generated. This is a relative package that is combined to the main package or parent package | Required |

Since a fragment can include other fragments, the resulting package is the combination of all packages in the branch.


