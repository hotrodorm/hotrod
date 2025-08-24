# Nitro

Nitro brings DynamicSQL functionality to defined queries. These queries can also take full advantage
of all native SQL extensions available in the database.

Nitro queries can be useful to:

- Use native SQL features to gain access to advanced database features, and to implement high performance queries.
- Use DynamicSQL to define queries that change their form at runtime by enabling, disabling, or rendering sections of the
query according to parameters values. See [DynamicSQL](./nitro-dynamicsql.md).
- Expose long, complex, and tedious queries as simple methods in classes.
- Use well-tested, pre-existent queries "as is" from your application.


## Example

The following query uses native DB2 features and Dynamic SQL:

```xml
<select method="findActiveAccountsWithClient" vo="AccountClient">
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

- [Nitro General Purpose Queries](nitro-general-purpose.md) &mdash; Queries that return no data.
- [Nitro Selects](nitro-selects.md) &mdash; SELECT queries that return a result set of rows.

## Parameters

To add parameters to the queries, including the use of JEXL syntax see [Parameters](./nitro-parameters.md).
