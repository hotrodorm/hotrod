# The `<facet>` Tag

A facet encloses a subset of the elements of the configuration file. The purpose of this differentiation
is to execute partial persistence layer generation, in order to refresh parts of the persistence layer only.

If the configuration file includes a vast number of tables, views, or Nitro queries refreshing the
persistence layer can take a significant amount of time. In such cases it may be convenient to refresh only the changing 
parts of it instead of its entirety.

Typically, `<facet>` tags identify the entries affected by the recent changes. Then, their names are provided to
the HotRod Generator as a comma-sepatated list of facet names. The HotRod Generator reads the entire 
configuration file(s), but refreshes only the sections included in the facets.


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the facet | Required |

A facet can include any number of `<table>`, `<view>`, `<enum>`, and `<dao>` tags in it.

