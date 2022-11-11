# The `<facet>` Tag

A facet encloses a subset of the elements of the configuration file, to refresh parts of the persistence
layer layer only.

If the configuration file includes a vast number of tables, views, or Nitro queries refreshing the
persistence layer can take a long time. In such cases it may be convenient to refresh only the changing 
parts of it instead of its entirety.

Typically, facets identify the entries affected by the latest changes. Their names are provided to 
the HotRod Generator, as a comma-sepatated list of facet names. The HotRod Generator reads the entire 
configuration, but refreshes only the sections included in the facets.


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the facet | Required |

A facet can include any number of `<table>`, `<view>`, `<enum>`, and `<dao>` tags in it.

