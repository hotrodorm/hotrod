# The Nitro Module

Nitro goes beyond the CRUD functionality and allows native and dynamic queries.

The main features of Nitro queries are:

- Reaching for High Performance: By using all native extensions of the SQL dialect that the database implements,
a developer with knowledge of SQL optimization can create queries that can achieve high performance.
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


