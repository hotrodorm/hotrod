# The CRUD Module

This is the CRUD Module of the [HotRod ORM](../README.md).

The CRUD module models tables, views, and their columns as classes with properties.

Tables and views are modeled as value objects (VOs) and their columns are modeled as properties of these VOs.

The persistence methods to retrieve and update data are modeled in separate Data Access Objects (DAOs) with
methods for each one. These include different variations of the traditional `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
operations according to the specifics of each table or view in the database.

The CRUD modeling is also used by LiveSQL queries and by Nitro's graph selects.


## The Entity VOs

VOs hold all data being retrieved from and sent to the database. They include one property for each database column
they represent. See [Value Objects](./value-objects.md) for details.


## The Entity DAOs

DAOs are classes that collect all persistence methods related to a table or view.

### Standard Entity Methods

CRUD automatically generates one DAOs for each included table and view. Each DAO includes basic persistence
methods to `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
on the database.

The generated methods differ between a table and a view:

| Persistence Method | Method on Table Entity | Method on View Entity | Optimistic Locking |
| -- | :-- | :-- | :--: |
| [Select by Primary Key](./select-by-primary-key.md) | select($\color{red}{pkColumns...}$) | &mdash; | &mdash; |
| [Select by Unique Index](./select-by-unique-index.md) | selectByUI$\color{red}{Col1}$$\color{blue}{Col2}$($\color{red}{Col1}$, $\color{blue}{Col2}$) | &mdash; | &mdash; |
| [Select by Example](./select-by-example.md) | select(example) | select(example) | &mdash; |
| [Select by Criteria](./select-by-criteria.md) | select(t, predicate) | select(v, predicate) | &mdash; |
| [Select Parent by Foreign Key](./select-parent-by-foreign-key.md) | selectParentProductOf(h).fromProductId().toId() | &mdash; | &mdash; |
| [Select Children by Foreign Key](./select-children-by-foreign-key.md) | selectChildrenPriceOf(p).fromId().toProductId() | &mdash; | &mdash; |
| [Insert](./insert.md) | insert(model) | &mdash; | &mdash; |
| [Insert By Example](./insert-by-example.md) | &mdash; | insert(model) [^1] | &mdash; |
| [Update By Primary Key](./update-by-primary-key.md) | update(model) | &mdash; | :heavy_check_mark: |
| [Update by Example](./update-by-example.md) | update(example, newValues) | update(example, newValues) [^1] | &mdash; |
| [Update by Criteria](./update-by-criteria.md) | update(newValues, t, predicate) | update(newValues, v, predicate) [^1] | &mdash; |
| [Delete by Primary Key](./delete-by-primary-key.md) | delete(pkColumns...) | &mdash; | :heavy_check_mark: |
| [Delete by Example](./delete-by-example.md) | delete(example) | delete(example) [^1] | &mdash; |
| [Delete by Criteria](./delete-by-criteria.md) | delete(t, predicate) | delete(v, predicate) [^1] | &mdash; |

[^1]: CRUD does not make a strong differentiation between tables and views. Typically databases do not inform
if a view is updatable or not, so CRUD adds data modification methods to all view DAOs. It's up to the developer
to decide if these methods can actually be used on each view or not. By and large databases consider a view updatable if it does have a 1:1 relationship with the underlying *driving* table and the primary key of this
table is available in the result set of the view. This is not written in stone, however, so it's crucial to
consult the specific database documentation to decide on this.

These methods are present or absent according to the specific details of each table or view. For example, if a table does not
have a primary key, then the method `select(pkColumns...)` will not be available for that table. Foreign keys and indexes also
affect the list of available methods in the Entity DAOs.


### Custom Entity Methods

Extra DAO methods can also be added to the entity DAOs using [Nitro](../nitro/README.md) functionality. A custom method
can be silent (no result set returned) or can return a list of rows. If they return rows, the entity DAO methods are
restricted to return Model objects of the spefic entity (table or view).

To retrieve custom result sets &mdash; either flat data rows, object tuples, graphs, or trees of objects &mdash; use the
general form of Nitro DAOs instead of the bounded form of the Entity DAOs.


## Configuration

With Discovery Mode enabled CRUD inspects an entire schema or schemas, and automatically generates the persistence
layer for all tables and views discovered in them.

With Discovery Mode disabled CRUD includes only the tables and views explicitly declared in the
[Configuration File](../config/configuration-file-structure.md).

CRUD can also mix the Discovery Mode with explicitly declared tables and views. In this case all tables and views
from both sources make up the persistence layer; the explicit configuration supersedes the details provided by the
discovery mechanism.

For each table and view included, CRUD adds one VO and one DAO to the persistence layer. VOs and DAOs are configured
with the [Generator](../config/tags/mybatis-spring.md) tag. VOs can be further customized with the
[Table](../config/tags/table.md) and [View](../config/tags/view.md) tags as well as
with the [Name Solver](../config/tags/name-solver.md) to define rules for VOs, DAOs, and property names, and the
[Type Solver](../config/tags/type-solver.md) tag to define rules for property types.


## Optimistic Locking

CRUD can be configured, on a per table basis, to implement optimistic locking. If this is enabled, then CRUD `SELECT` queries
on the specific table get automatically related to CRUD data modification queries (`UPDATE` and `DELETE`) using the VO values. 
If changes produced by other threads or processes are detected in the row then the data modification query is aborted and an
exception is thrown, with the aim of aborting the transaction.

See [Version Control](../config/tags/version-control-column.md) for details on how to activate this feature.
See [Update by Primary Key](./update-by-primary-key.md) and [Delete by Primary Key](./delete-by-primary-key.md) for details
on how this functionality affects these operations.







