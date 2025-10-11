# The `<layer-resources>` Tag

This tag configures the generation of the layer resources class that holds the layer properties.

## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `base-dir` | Overrides the base dir specified in the parent `<jdbc>` tag | *parent base-dir* |
| `package`  | Overrides the package specified in the parent `<jdbc>` tag  | *parent package* |
| `sub-package`  | Specifies the extra sub-package for the layer resources bean | `resources` |
| `name` | Specifies the class name of the layer resources bean | `LayerResourcesBean` |


