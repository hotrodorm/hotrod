# The CRUD Module

The CRUD module models tables, views, and their columns as classes with properties. It also adds methods
for persistence operations for basic `SELECT`, `INSERT`, `UPDATE`, and `DELETE`.

Tables and view are modeled as value objects (VOs) and their columns are modeled as properties of these VOs. 
The persistence methods to retrieve and update data are modeled in separate Data Access Objects (DAOs) with
methods for each one.

The CRUD generator inpects the database and takes a few seconds to generate all these classes.

The CRUD model is used by LiveSQL queries and by the structured selects of the Nitro module.


## The Value Objects

VOs hold all data being retrieved from and sent to the database. They include one property for each database column
they represent. See [Value Objects](./value-objects.md) for details.


## The DAOs

CRUD generates data access objects (DAOs) with basic persistence methods to `SELECT`, `INSERT`, `UPDATE`, and `DELETE`.
These include:

- [Select by Primary Key]().
- [Select by Unique Indexes]().
- [Select by Example]().
- [Select by Criteria]().
- [Select Parent by Foreign Key]().
- [Select Children by Foreign Key]().
- [Insert]().
- [Update By Primary Key]().
- [Update by Example]().
- [Delete by Primary Key]().
- [Delete by Example]().

For more advanced or custom queries see the [Nitro Module](../nitro/nitro.md).


## Views

CRUD does not make a strong differentiation between tables and views. Typically databases do not inform if a view is
updatable or not, so CRUD adds data modification methods to all view DAOs. It's up to the developer to decide if these
methods can actually be used on each view or not.

By and large database consider a view updatable if it does have a 1:1 relationship with the underlying *driving*
table and the primary key of this table is available in the result set of the view. This is not written in stone,
however, so it's crucial to consult the specific database documentation to decide on this.


## Selection and Configuration

CRUD does not inspect the entire database for tables and views. Each table and view mentioned in the
[Configuration File](../config/configuration-file-structure.md) adds one VO and one DAO to the persistence layer.

Once selected, VOs and DAOs are configured with the [Generator](../config/tags/mybatis-spring.md) tag. VOs can be
further customized with the [Table](../config/tags/table.md) and [View](../config/tags/view.md) tags as well as 
with the [Name Solver](../config/tags/name-solver.md) and [Type Solver](../config/tags/type-solver.md) tags.


## Optimistic Locking

CRUD can be configured, on a per table basis, to implement optimistic locking. If this is enabled, then CRUD `SELECT` queries
get automatically related to CRUD data modification queries (`UPDATE` and `DELETE`) using the VO values. If changes
produced by other threads of processed are detected in the row then the data modification query is aborted and an
exception is thrown, with the aim of aborting the transaction.

See [Version Control](../config/tags/version-control-column.md) for details on how to activate this feature.





