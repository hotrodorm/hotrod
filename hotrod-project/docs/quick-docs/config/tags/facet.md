# The `<facet>` Tag

A facet enclosed a subset of the elements of the configuration file, that could be generated separately.

If the configuration file includes a significant number of Nitro queries refreshing the persistence layer
can take a long time. In such cases if could be convenient to refresh only the changing part of the 
persistence layer and not its entirety. 

Typically one or more facets with the latest changes can be defined and then their names can be provided to 
HotRod (as a comma-sepatated list of names). The HotRod Generator will read the entire configuration, but 
will refresh only the section included in the facet(s).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the facet | Required |

A facet can include any number of `<table>`, `<view>`, `<enum>`, and `<dao>` tags in it.

