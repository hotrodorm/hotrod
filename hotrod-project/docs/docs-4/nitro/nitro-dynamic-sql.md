# Dynamic SQL

Dynamic SQL can be used in all [Nitro](./nitro.md) queries to include or exclude fragments of the SQL query 
at runtime based on the parameter values. In short, all `<select>` and `<query>` tags can include Dynamic SQL.

Dynamic SQL can alter the SQL fragments by applying or injecting parameter values, by trimming sections 
of them, or by replicating them with iterators.

The design of the Dynamic SQL tags was influenced by the MyBatis framework
(see [MyBatis Dynamic SQL](https://mybatis.org/mybatis-3/dynamic-sql.html)) but HotRod models it independently,
so any generator or underlying JDBC technology can use it.


## OGNL - The Ogonal Engine

Dynamic SQL decides to include or exclude SQL fragments based on boolean logic evaluated at runtime according to the supplied parameters. This boolean logic is expressed in OGNL scripting from [the OGNL Engine](https://commons.apache.org/proper/commons-ognl/language-guide.html).

For example, the following expressions are valid ones in OGNL:

- `name != null`
- `phase == 'C' or amount > minAmount`
- `orderDate != null ? status in (1, 3, 4) : status in (null, 2)`

It's important to remember the expressions must evaluate to a boolean value, either `true` or `false`. The variables such as `name`, `phase`, `amount`, etc. correspond to runtime parameters of the query, specified using `<parameter>` tags.


## The `<if>` Tag

The `<if>` tag includes the inner SQL segment depending on the value `test` condition evaluated at runtime. If it evaluates to `true` the inner segment is included; otherwise it's ignored.

For example:

```xml
<select method="searchPendingOrders" vo="OrderVO">
  <parameter name="minPrice" java-type="Double" />
  select * from orders
  where status = 'PENDING'
  <if test="minPrice != null">and order_price >= #{minPrice}</if>
</select> 
```

The above query searches for pending orders. If the supplied parameter `minPrice` is not null, the query adds the extra condition `and order_price >= #{minPrice}` to the search predicate that, otherwise, won't be included.


## The `<choose>`, `<when>`, and `<otherwise>` Tags

The `<choose>` tag is a variation of the `<if>` tag that allows multiple exclusive conditions to be evaluated sequentially. Each fragment is enclosed in a `<when>` tag that includes a condition. The first `<when>` tag with a matching condition is selected and its SQL fragment is added to the SQL statement. The remaining fragments are not evaluated. If no `<when>` tag is selected, the `<otherwise>` segment is selected, if present.

For example:

```xml
<select method="searchPendingOrders" vo="OrderVO">
  <parameter name="searchType" java-type="String" />
  <parameter name="minPrice" java-type="Double" />
  <parameter name="orderDate" java-type="java.util.Date" />
  select * from orders
  where status = 'PENDING'
  <choose>
    <when test="type == 'PRICE'">and order_price >= #{minPrice}</when>
    <when test="type == 'DATE'">and order_date = #{orderDate}</when>
    <otherwise>and channel = 'ONLINE'</otherwise>
  </choose>
</select> 
```

The above query searches for pending orders. If the `type` parameter has the value `PRICE` it uses the `minPrice` parameter to search for orders; otherwise, if 
the `type` parameter has the value `DATE` it uses the `orderDate` parameter to search for orders; if none of these options are used it defaults to searching by `channel = 'ONLINE'`.


## The `<where>` Tag

The `<where>` tag encloses multiple inner tags. If at least one of them is included it does two things:

- It prepends the whole section with a `WHERE` clause.
- It removes any `AND` or `OR` from the **first** selected inner fragment. Therefore, if multiple fragments are included they will render appropriately.

For example:

```xml
  <select method="searchInvoices" vo="InvoiceVO">
    <parameter name="branchId" java-type="Integer" />
    <parameter name="clientId" java-type="Integer" />
    <parameter name="minAmount" java-type="Double" />
    select * from invoice
    <where>
      <if test="branchID != null">and branch_id = #{branchId}</when>
      <if test="clientID != null">and client_id = #{clientId}</when>
      <if test="minAmount != null">and amount >= #{minAmount}</when>
    </where>
  </select> 
```

If the caller supplies the values (branchId = `301`, clientId = `null`, minAmount = `20`) the query will be assembled as:

```sql
select * from invoice
where
  branch_id = 301
  and amount >= 20
```

Notice:
- The `WHERE` clause was included, since at least one inner tag was included.
- The `AND` in `and branch_id = 301` was removed, since this is the first included tag.
- The `AND` in `and amount >= 20` was not removed, since this is not the first included tag.
- The second inner tag was not included, since its condition was not met.


## The `<set>` Tag

The `<set>` tag has a very similar functionality as the `<where>` tag but it's tailored for `UPDATE` SQL statements.

If at least one of the inner fragments is included it does two things:

- It prepends the whole section with a `SET` clause.
- It removes any `,` from the **first** selected inner fragment. Therefore, if multiple fragments are included they will render appropriately.

For example:

```xml
<query method="markOutstandingInvoices">
  <parameter name="newStatus" java-type="String" />
  <parameter name="dueDate" java-type="java.util.Date" />
  update invoice
  <set>
    <if test="newStatus != null">, invoice_status = #{newStatus}</when>
    <if test="dueDate != null">, invoice_due_date = #{dueDate}</when>
  </set>
  where total_amount_due > amount_paid      
</query>
```

If the caller supplies the values (newStatus = `null`, dueDate = `2020-12-01`) the query will be assembled as:

```sql
update invoice
set
  invoice_due_date = '2020-12-01'
where total_amount_due > amount_paid      
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

Therefore:

- A `<trim prefix='WHERE' prefixOverrifes="AND|OR">` tag is equivalent to a `<where>` tag.
- A `<trim prefix='SET' prefixOverrifes=",">` tag is equivalent to a `<set>` tag.


## The `<foreach>` Tag

The `<foreach>` tag iterated over a `java.util.Collection` or an array and includes the inner SQL fragment once for each iterated element.

For example:

```xml
<select method="findEmployees" vo="EmployeeVO">
  <parameter name="ids" java-type="java.util.List&lt;Integer>" jdbc-type="NUMERIC" />
  <parameter name="names" java-type="java.util.List&lt;String>" jdbc-type="VARCHAR" />
  select *
  from employee
  <complement>
    where branch_id in
    <foreach item="id" collection="ids" open="(" separator=", " close=")">
      #{id}
    </foreach>
    or name in
    <foreach item="name" collection="names" open="(" separator=", " close=")">
      #{name}
    </foreach>
  </complement>
</select>
```

Depending on the specific parameters the query will change. If the first list has three values and the second one two, the query will be assembled as:

```sql
select * from employee
where branch_id in (?, ?, ?)
  and name in (?, ?)
```

The parameters that will be *applied* to the query could in this case be:

```
101 (Integer), 102 (Integer), 200 (Integer), Alice (String), Steve (String)
```

These case be displayed by enabling the DEBUG level in the logging of the query.

Finally, there's of course a performance penalty when using large collections or arrays. Also, in the case of large collections or arrays,
some database engines and JDBC drivers may place a limit in the size of the SQL statement. Most database engines will accept 
1000-character long SQL statements, but may reject 10000-character long SQL statements.


## The `<bind>` Tag

The `<bind>` tag allows the developer to set temporary variables in the Dynamic SQL scope that can help the writing of complex expressions.

For example:

```xml
<select method="findClientsByPartialName" vo="ClientVO">
  <parameter name="partialName" java-type="String" />
  <bind name="namePattern" value="'%' || partialName || '%'" />
  select * from client
  where name like #{namePattern}
</select> 
```

If the supplied parameters at runtime are (partialName = `"smith"`) the query will be assembled as:

```sql
select * from client
where name like '%smith%'
```


