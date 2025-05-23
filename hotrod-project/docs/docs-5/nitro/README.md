# The Nitro Module

Nitro goes beyond the CRUD functionality to allow the use native SQL code and dynamic queries to be executed.

Nitro queries can:

- Use native SQL features to gain access to advanced database features.
- Use native SQL features to implement high performance queries.
- Use DynamicSQL to define queries that change their form at runtime by enabling, disabling, or rendering sections of the
query according to parameters values. See [DynamicSQL](./nitro-dynamicsql.md). 


## Example

The following query uses native DB2 features and Dynamic SQL:

```xml
<select method="findActiveAccountsWithClient" vo="AccountClientVO">
  <parameter name="regionId" java-type="Integer" />
  SELECT a.*, c.name, c.type as "client_type"
  FROM account a
  JOIN client c ON c.id = a.client_id
  WHERE a.balance >= 1000.00 SELECTIVITY 0.0013 
  <if test="regionId != null">
    AND c.region_id = #{regionId} SELECTIVITY 0.07
  </if>
</select>
```

Depending on the value of the `regionId` parameter the query changes form. Note that the `SELECTIVITY` clauses are a part of the DB2 dialect
that allows the developer to tune query performance.


## Nitro Queries

There are two types of Nitro queries. They both can include Native and DynamicSQL functionality:

- [General Purpose Queries](nitro-general-purpose.md) &mdash; Queries that return no data.
- [Flat Selects](nitro-flat-selects.md) &mdash; Traditional selects that automate names and property types.


