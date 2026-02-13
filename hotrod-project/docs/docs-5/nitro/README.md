# Nitro

Nitro allows you to combines native SQL with Dynamic SQL to access all SQL features available in the SQL dialect.

In short, Nitro queries can be useful to:

- Run any SQL statement beyond SELECT, INSERT, UPDATE, and DELETE, including calling stored procedures and functions
- Use [Dynamic SQL](./hotrod-project/docs/docs-5/nitro/nitro-dynamicsql.md) to dynamically include query sections into the main query according to the runtime parameters
- Use native SQL extensions available in the specific database, such as optimizer hints, regular expression matching, custom functions, full text search, rollups, etc., and implement high performance queries
- Expose long, complex, and tedious queries as simple methods in your app
- Use pre-existent and well-tested queries "as is" from your application

Get started with the [Hello Nitro](../guides/hello-nitro.md) example.


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

# Nitro

## Example

## Update Queries

## Select Queries

### Entity Select Queries

### Free Select Queries

### Layout and Model Classes

### Property Names

### Property Types

### Fetch Mode

### The Select Foundation and Complement

## Native SQL

## Nitro Dynamic SQL

## Parameters

### Applied Parameters

### SQL Injection Parameters

### Parameter Declaration and Occurrences




## Nitro Queries

There are two types of Nitro queries. They both can include Native and DynamicSQL functionality:

- [Nitro General Purpose Queries](nitro-general-purpose.md) &mdash; Queries that return no data.
- [Nitro Selects](nitro-selects.md) &mdash; SELECT queries that return a result set of rows.

## Parameters

To add parameters to the queries, including the use of JEXL syntax see [Parameters](./nitro-parameters.md).
