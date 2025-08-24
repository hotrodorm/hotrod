# CRUD

CRUD models schema tables and views as fully typed classes. They can be used in CRUD queries as well as in LiveSQL queries.

Tables and views are modeled as Layout and Model classes and their columns are modeled as properties of these.

The persistence methods to retrieve and update data are modeled in separate Data Access Objects (DAOs) with
methods for each one. These include different variations of the traditional `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
operations according to the specifics of each table or view in the database.

The CRUD modeling is also used by LiveSQL queries and by Nitro's selects.


## The Layout and Model Classes

The data retrieved by the persistence layer is typically modeled with a separate layout and model classes. See [Database Row Modeling](./database-row-modeling.md) for details.


## The Entity DAOs

DAOs are classes that collect all persistence methods related to a table or view.


### Entity Methods

CRUD automatically generates one DAOs for each included table and view. Each DAO includes basic persistence
methods to `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
on the database.

The generated methods differ between a table and a view:

| Persistence Method | On Table Entity | On View Entity | Optimistic Locking |
| -- | :-- | :-- | :--: |
| [Select by Primary Key](./select-by-primary-key.md) | select(<b>pkColumns</b>...) | &mdash; | &mdash; |
| [Select by Example](./select-by-example.md) | select(example) | select(example) | &mdash; |
| [Select by Criteria](./select-by-criteria.md) | select(t, predicate) | select(v, predicate) | &mdash; |
| [Insert](./insert.md) | insert(model) | insert(model) | &mdash; |
| [Insert By Example](./insert-by-example.md) | insert(model) | insert(model) [^1] | &mdash; |
| [Update By Primary Key](./update-by-primary-key.md) | update(model) | &mdash; | :heavy_check_mark: |
| [Update by Example](./update-by-example.md) | update(example, newValues) | update(example, newValues) [^1] | &mdash; |
| [Update by Criteria](./update-by-criteria.md) | update(newValues, t, predicate) | update(newValues, v, predicate) [^1] | &mdash; |
| [Delete by Primary Key](./delete-by-primary-key.md) | delete(<b>pkColumns</b>...) | &mdash; | :heavy_check_mark: |
| [Delete by Example](./delete-by-example.md) | delete(example) | delete(example) [^1] | &mdash; |
| [Delete by Criteria](./delete-by-criteria.md) | delete(t, predicate) | delete(v, predicate) [^1] | &mdash; |

[^1]: CRUD does not make a strong differentiation between tables and views. Typically databases do not inform
if a view is updatable or not, so CRUD adds data modification methods to all view DAOs. It's up to the developer
to decide if these methods can actually be used on each view or not. By and large databases consider a view updatable if it does have a 1:1 relationship with the underlying *driving* table and the primary key of this
table is available in the result set of the view. This is not written in stone, however, so it's crucial to
consult the specific database documentation to decide on this.

These methods are present or absent according to the specifics of each table. For example, if a table does not
have a primary key, then the method `select(pkColumns...)` will not be available for that table. 


### Custom Entity Methods

Extra DAO methods can also be added to the entity DAOs using [Nitro](../nitro/README.md) functionality. A custom method
can be silent (no result set returned) or can return a list of rows. If they return rows, the entity DAO methods are
restricted to return Model objects of the spefic entity (table or view).

To retrieve custom result sets use the general form of Nitro DAOs in using the `<dao>` tags, instead of the bounded form of the Entity DAOs.


## Configuration

With Discovery Mode enabled CRUD inspects an entire schema or schemas, and automatically generates the persistence
layer for all tables and views discovered in them.

With Discovery Mode disabled CRUD includes only the tables and views explicitly declared in the
[Configuration File](../config/README.md).

CRUD can also mix the Discovery Mode with explicitly declared tables and views. In this case all tables and views
from both sources make up the persistence layer; the explicit configuration supersedes the details provided by the
discovery mechanism.

For each table and view included, CRUD adds one Layout class, one Model class, and one DAO to the persistence layer. The Layout, Model, and DAOs classes can be configured using the `<dao>`, `<layout>`, and `<model>` tags within the with the [`<jdbc>`](../config/tags/jdbc.md) tag. The Layout and Model clasess can be further customized with the
[Table](../config/tags/table.md) and [View](../config/tags/view.md) tags as well as
with the [Name Solver](../config/tags/name-solver.md).

The [Type Solver](../config/tags/type-solver.md) defines rules to decide property types.


## Optimistic Locking

CRUD can be configured, on a per table basis, to implement optimistic locking. If this is enabled,
then CRUD `SELECT` queries on the specific table get automatically related to CRUD data modification queries, `UPDATE` and `DELETE`.

If changes produced by other threads or processes are detected in the row then the data modification
query is aborted and an exception is thrown, to invalidate the transaction.

See [Optimistic Locking](../config/tags/optimistic-locking.md) for details on how to activate this feature.







