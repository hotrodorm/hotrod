# DynamicSQL

DynamicSQL can be used in all [Nitro](./README.md) queries to include or exclude fragments of a SQL query 
at runtime based on the parameter values. All Nitro queries &mdash; declared with `<select>` and `<query>`
tags &mdash; can include DynamicSQL sections.

For example, if a query needs to conditionally include one sections of it, DynamicSQL can do this as shown below::

```xml
  <select method="getVIPProviders" vo="ProviderVO">
    <parameter name="branchId" java-type="Long" />
      SELECT *
      FROM providers
      WHERE type = 'VIP
      <if test="branchId != null">
        AND branch_id = #{branchId}
      </if>
  </select> 
```

In this case the section `AND branch_id = #{branchId}` will be included only when the parameter `branchId` is not null.


## Parameters

All Nitro queries can have parameters. These can be applied or injected directly in the query, can
also be used to govern DynamicSQL, or both. From the application's perspective these parameters are
presented as parameters in the DAO method that executes the query.

For details on the definition and usage of parameters see [Nitro Parameters](./nitro-parameters.md).


## The JEXL Expression Language

DynamicSQL includes or excludes query sections based on boolean logic implemented in JEXL syntax that is
evaluated at runtime. The JEXL expression language is described at [Apache JEXL](https://commons.apache.org/proper/commons-jexl/).

For example, the following expressions are written in JEXL:

- `!empty name`
- `name != null`
- `phase == 'C' or amount > minAmount`
- `orderDate != null ? status in (1, 3, 4) : status in (null, 2)`

When they are used as predicate conditions they need to evaluate to a boolean value &mdash; either `true` or `false`. They can, 
however, evaluate to any Java type, as needed: for example, the `<bind>` tag can use any resulting type. In the examples above, the
variables such as `name`, `phase`, `amount`, etc. correspond to runtime parameters of the query, specified using `<parameter>` tags.


## The `<if>` Tag

The `<if>` tag includes the inner SQL segment depending on the value `test` condition evaluated at runtime. If it evaluates to `true` the inner segment is included; otherwise it's ignored.

For example:

```xml
  <select method="searchPendingOrders" vo="OrderVO">
    <parameter name="minPrice" java-type="Double" />
    SELECT *
    FROM orders
    WHERE status = 'PENDING'
    <if test="minPrice != null">AND order_price >= #{minPrice}</if>
  </select> 
```

The above query searches for pending orders. If the supplied parameter `minPrice` is not null, the query adds the extra condition `AND order_price >= #{minPrice}` to the search predicate that, otherwise, won't be included.


## The `<choose>`, `<when>`, and `<otherwise>` Tags

The `<choose>` tag is a variation of the `<if>` tag that allows multiple exclusive conditions to be evaluated sequentially. Each fragment is enclosed in a `<when>` tag that includes a condition. The first `<when>` tag with a matching condition is selected and its SQL fragment is added to the SQL statement. The remaining fragments are not evaluated. If no `<when>` tag is selected, the `<otherwise>` segment is selected, if present.

**Note**: The `<choose>` tag cannot directly include SQL content, but only `<when>` and `<otherwise>` tags.

For example:

```xml
  <select method="searchPendingOrders" vo="OrderVO">
    <parameter name="searchType" java-type="String" />
    <parameter name="minPrice" java-type="Double" />
    <parameter name="orderDate" java-type="java.util.Date" />
    SELECT *
    FROM orders
    WHERE status = 'PENDING'
    <choose>
      <when test="type == 'PRICE'">AND order_price >= #{minPrice}</when>
      <when test="type == 'DATE'">AND order_date = #{orderDate}</when>
      <otherwise>AND channel = 'ONLINE'</otherwise>
    </choose>
  </select> 
```

The above query searches for pending orders. If the `type` parameter has the value `PRICE` it uses the `minPrice` parameter to search for orders; otherwise, if 
the `type` parameter has the value `DATE` it uses the `orderDate` parameter to search for orders; if none of these options are selected, then it defaults to searching by `channel = 'ONLINE'`.


## The `<where>` Tag

The `<where>` tag encloses multiple `<if>` inner tags. If at least one of them is included it does two things:

- It prepends the whole section with a `WHERE` clause.
- It removes any `AND` or `OR` from the first selected inner fragment.

**Note**: The `<where>` tag cannot directly include SQL content, but only `<if>` tags.

For example:

```xml
  <select method="searchInvoices" vo="InvoiceVO">
    <parameter name="branchId" java-type="Integer" />
    <parameter name="clientId" java-type="Integer" />
    <parameter name="minAmount" java-type="Double" />
    SELECT *
    FROM invoice
    <where>
      <if test="branchID != null">AND branch_id = #{branchId}</when>
      <if test="clientID != null">AND client_id = #{clientId}</when>
      <if test="minAmount != null">AND amount >= #{minAmount}</when>
    </where>
  </select> 
```

If the caller supplies the values (branchId = `301`, clientId = `null`, minAmount = `20`) the query will be assembled as:

```sql
  SELECT *
  FROM invoice
  WHERE branch_id = 301
    AND amount >= 20
```

Notice:
- The `WHERE` clause was included, since at least one inner tag was included.
- The `AND` in `AND branch_id = 301` was removed, since this is the first included tag.
- The `AND` in `AND amount >= 20` was not removed, since this is not the first included tag.
- The second inner tag was not included, since its condition was not met.


## The `<set>` Tag

The `<set>` tag has a very similar functionality as the `<where>` tag but it's tailored for `UPDATE` SQL statements.

If at least one of the inner fragments is included it does two things:

- It prepends the whole section with a `SET` clause.
- It removes any `,` from the first selected inner fragment.

**Note**: The `<set>` tag cannot directly include SQL content, but only `<if>` tags.

For example:

```xml
  <query method="markOutstandingInvoices">
    <parameter name="newStatus" java-type="String" />
    <parameter name="dueDate" java-type="java.util.Date" />
    UPDATE invoice
    <set>
      <if test="newStatus != null">, invoice_status = #{newStatus}</when>
      <if test="dueDate != null">, invoice_due_date = #{dueDate}</when>
    </set>
    WHERE total_amount_due > amount_paid      
  </query>
```

If the caller supplies the values (newStatus = `null`, dueDate = `2020-12-01`) the query will be assembled as:

```sql
  UPDATE invoice
  SET invoice_due_date = '2020-12-01'
  WHERE total_amount_due > amount_paid      
```

Notice that:
- The `SET` clause was included, since at least one inner tag was included.
- The `,` in `, invoice_due_date = '2020-12-01'` was removed, since this is the first included tag.
- The first inner tag was not included, since its condition was not met.


## The `<trim>` Tag

The `<trim>` tag is a generic form of the `<where>` and `<set>` tags. The developer can use the generic form to specify:

- The `prefix` to prepend to the whole fragment, when at least one inner fragment is included.
- The `suffix` to append to the whole fragment, when at least one inner fragment is included.
- The `prefixOverrides` indicates the prefixes to remove from the first selected inner fragment; the list is separated by the `|` (pipe) character.
- The `suffixOverrides` indicates the suffixes to remove from the last selected inner fragment; the list is separated by the `|` (pipe) character.

**Note**: The `<TRIM>` tag cannot directly include SQL content, but only `<if>` tags.

Therefore:

- A `<trim prefix='WHERE' prefixOverrifes="AND|OR">` tag is equivalent to a `<where>` tag.
- A `<trim prefix='SET' prefixOverrifes=",">` tag is equivalent to a `<set>` tag.


## The `<foreach>` Tag

The `<foreach>` tag iterates over a collection or array and includes the inner SQL fragment once for each element.

For example:

```xml
<select method="findEmployees" vo="EmployeeVO">
  <parameter name="ids" java-type="java.util.List&lt;Integer>" jdbc-type="NUMERIC" />
  <parameter name="names" java-type="java.util.List&lt;String>" jdbc-type="VARCHAR" />
  SELECT *
  FROM employee
    <complement>
    WHERE branch_id IN
    <foreach item="id" collection="ids" open="(" separator=", " close=")">
      #{id}
    </foreach>
    OR name IN
    <foreach item="name" collection="names" open="(" separator=", " close=")">
      #{name}
    </foreach>
    </complement>
</select>
```

Depending on the specific parameters the query will change. If the first list has three values and the second one two, the query will be assembled as:

```sql
SELECT *
FROM employee
WHERE branch_id IN (?, ?, ?)
  OR name IN (?, ?)
```

The parameters that will be *applied* to the query could in this case be:

```
101 (Integer), 102 (Integer), 200 (Integer), Alice (String), Steve (String)
```

These parameter values can be displayed by enabling the DEBUG level in the logging of the query.

Finally, there's of course a performance penalty when using large collections or arrays. Also, in the case of large collections or arrays,
some database engines and JDBC drivers may place a limit in the size of the SQL statement. Most database engines will accept 
1000-character long SQL statements, but may reject 30000-character long SQL statements.


## The `<bind>` Tag

The `<bind>` tag allows the developer to set temporary variables in the DynamicSQL scope that can help the writing of complex expressions.

For example:

```xml
<select method="findClientsByPartialName" vo="ClientVO">
  <parameter name="partialName" java-type="String" />
  <bind name="namePattern" value="'%' || partialName || '%'" />
  SELECT *
  FROM client
  WHERE name LIKE #{namePattern}
</select> 
```

If the supplied parameters at runtime are (partialName = `"smith"`) the query will be assembled as:

```sql
select * from client
where name like '%smith%'
```


