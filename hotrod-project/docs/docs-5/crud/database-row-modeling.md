# Database Row Modeling

In HotRod, data rows coming from a SELECT query are typically modeled as Java classes with properties with clearly defined names and types.

The main case applies to data being retrieved from a table or view, where the data structure is well known. The other case applies to data being retrieved from a Nitro query. In both cases, the result
set of the query is inspected to produce the classes that will hold the result when running the query.

Now, for design purposes, the HotRod produces two classes for each modeled row:

- The **Layout** class that includes the table, view, or SELECT query structure. This class includes
properties for each column of the table, view or SELECT query, with their corresponding data types, setters, and getters. This class includes no methods beyond the basic ones.
- The **Model** class that inherits from the Layout class. This class is virtually empty and is meant to
be enhanced by the developer to add extra properties and methods as needed.


## Updating The Persistence Layer

Since the columns of tables, views, and SELECT queries will vary over time, the generator can be executed again to discover the updated structures and update the persistence layer.

The generator will always overwrite the Layout class so it mimics the current database structure. Any
new columns will show up in the layout classes automatically. Removed or updated colums will also be
removed and updated from the persistence layer. If the column naming rules and/or the type solver rules were changed, then this will also be updated accordingly in the Layour class.

The Model class, on the other hand, will never be automatically updated. This class may include custom
code by the developer and should not be overwritten.

## Nitro

Since Nitro SELECTs retrieve data rows from the database they are also modeled this way. Even when
having a more fleeting nature the data modeling still follows the same strategy of Layout and Model clasess.





