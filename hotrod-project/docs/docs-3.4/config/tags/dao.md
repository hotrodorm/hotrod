# The `<dao>` Tag

A `<dao>` tag adds one DAO class to the persistence layer. This DAO class is meant to include methods according to the
inner `<sequence>`, `<query>`, and `<select>` tags.


## Attributes

The `<dao>` tag include the following attribute:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The Java base name for the DAO | Required |


## Persistence Methods

The DAO class can have any number of included [`<sequence>`](sequence.md), [`<query>`](query.md),
and [`<select>`](select.md). These tags can be included in any order and can be intermixed. Each one produces
one method in the DAO.





