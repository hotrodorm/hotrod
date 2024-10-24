# The Nitro Module

This is the Nitro Module of the [HotRod ORM](../README.md).

Nitro assists the developer on writing native queries and configuration-assisted queries.

Nitro goes beyond the CRUD functionality and allows the developer to execute native queries in the database.
These queries can be plain native queries or configuration-assisted queries that take advantage of the knowledge
the CRUD module has on the database to simplify their writing. These queries are added to the DAOs in the
persistence layer and are exposed as Java methods.

The main features of Nitro queries are:

- Reaching for High Performance: By using all native extensions of the SQL dialect that the database implements,
a developer with knowledge of SQL optimization can create queries that can achieve high performance.
- Make the most of the CRUD model: Nitro queries make use of the CRUD model to allow configuration-assisted
queries that can retrieve data into non-trivial data structures.
- Dynamic SQL: Dynamic SQL allows queries to change their form by enabling, disabling, or rendering sections of the
query based on runtime parameters. See [Dynamic SQL](nitro-dynamic-sql.md). 


## Example

The following query (a Flat Select) uses native DB2 features and Dynamic SQL:

```xml
<select method="findActiveAccountsWithClient" vo="AccountClientVO">
  <parameter name="regionId" java-type="Integer" />
  select a.*, c.name, c.type as "client_type"
  from account a
  join client c on c.id = a.client_id
  where a.balance >= 1000.00 SELECTIVITY 0.0013 
  <if test="regionId != null">
    and c.region_id = #{regionId} SELECTIVITY 0.07
  </if>
</select>
```

Depending on the value of the `regionId` parameter the query changes shape. The `SELECTIVITY` clauses are a part of the DB2 dialect
that allows the developer to tune query performance.


## Nitro Queries

There are three types of Nitro queries. All of them can include Native and Dynamic SQL:

- [General Purpose Queries](nitro-general-purpose.md) &mdash; Queries that return no data.
- [Flat Selects](nitro-flat-selects.md) &mdash; Traditional selects that automate names and property types.
- [Graph Selects](nitro-graph-selects.md) &mdash; Selects that return graphs of objects.


