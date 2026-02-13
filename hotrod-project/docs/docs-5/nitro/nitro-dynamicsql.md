# Nitro Dynamic SQL

Nitro Dynamic SQL can be used in all [Nitro](./README.md) queries to execute SQL queries that combine static and dynamic sections in them. The dynamic sections are automatically activated or deactivated according to the specified logic and according to the parameters that are provided at runtime.

All Nitro queries &mdash; declared with `<select>` and `<query>` tags &mdash; can include Nitro Dynamic SQL sections.

The following example includes a dynamic query that updates a table:

```xml
<select method="getVIPProviders" vo="Provider">
  <parameter name="branchId" java-type="Integer" />
    SELECT *
    FROM providers
    WHERE type = 'VIP
    <if test="branchId != null">
      AND branch_id = #{branchId}
    </if>
</select>
```

In this case the section `AND branch_id = #{branchId}` will be included only when the parameter `branchId` is not null.


## 1. Previewing Queries and JDBC Parameters

Since the query structure may change in every execution sometimes is useful to find out exactly how the nitro query is assembled prior to being executed.

To do this you can enable logging in the DAO class that includes the query method. For example, if the DAO class that includes this Nitro query was `app.persistence.dao.ReportingDAO` and if we provided the parameter value `125` for the branchId we could enable logging in the `application.properties` file by adding:

```properties
logging.level.app.persistence.dao.ReportingDAO=DEBUG
```

The DEBUG level will log the actual query being executed. For example, the query above could be logged as:

```
  SELECT *
  FROM providers
  WHERE type = 'VIP
    AND branch_id = ?
```

Enabling the TRACE level will log the query being executed and also the JDBC parameters being applied to it. In this case the query above could be logged as:

```
  SELECT *
  FROM providers
  WHERE type = 'VIP
    AND branch_id = ?

  JDBC Parameters (1):
    1. branchId: 123 (java.lang.Integer)
```


## 2. Defining Parameters

Nitro queries can have parameters that can be used in the query, can be used to govern DynamicSQL, or both.

To define a query parameter use the `<parameter>` tag. See [Nitro Parameters](./nitro-parameters.md) for details.

Since Nitro queries are defined inside DAOs, the are exposed as methods of these DAOs. Each method includes the list of parameters defined with `<parameter>` tags.


## 3. Using Parameters

When using parameters in the queries these are referenced using JEXL syntax. This syntax allows the access of simple parameters and also complex ones, such as arrays, beans, lists, collections, etc. Again, see [Nitro Parameters](./nitro-parameters.md) for details.


## 4. DynamicSQL

Dynamic SQL includes the following tags:


| Operator | Description |
| -- | -- |
| `<if>` | Conditionally include the inner content if the test condition evaluates to true |
| `<choose>` | Include only the first `<when>` content that evaluates to true and ignore the rest. If an `<otherwise>` tag is included at the end, then include this one if all `<when>` tags failed the test condition |
| `<foreach>` | Iterate over an collection or array of elements. The inner content is included &mdash; and reevaluated &mdash; once per each element |
| `<bind>` | Binds a variable in the parameter scope, so it can be used by other tags or content. Once a variable is bound in a scope, it cannot be rebinded in that scope |
| `<trim>` | A trim section includes multiple `<if>` tags; trim will collect all inner content that evaluated to true and will join them with a specified separator |
| `<where>` | A variation of the trim tag tailored to be used as a WHERE clause; each inner `<if>` can potentially be include or excluded, and this tags joins them using and AND or OR operators |
| `<set>` | A variation o f the trim tag tailored to be used as the SET clause of an UPDATE statement; each inner `<if>` can potentially be include or excluded, and the set section joins them using commas |


### 4.1 The &lt;if> Tag

The IF tag is one of the simplest operators. It includes a `test` predicate and the nested content can include SQL sections, parameters inclusion, and other DynamicSQL operators. The content is only included if the test predicate evaluates to true at runtime.

The following example illustrates how an IF tag works. In this example, the main IF tag includes nested IF tags:

```xml
<select method="searchEmployees" vo="Employee">
  <parameter name="f" type="app.data.EmployeeFilter" />
  SELECT * FROM employee WHERE active = 'Y'
  <if test="f != null">
    <if test="f.firstName != null"> AND first_name = #{f.firstName}</if>
    <if test="f.lastName != null"> AND last_name = #{f.lastName}</if>
    <if test="f.firstSSN != null"> AND last_ssn = #{f.lastSSN}</if>
  </if>
</select>
```

The inner IF tags will only be evaluated if the parent IF evaluates to true; otherwise, they'll be fully ignored and excluded.

An IF tag can nest static SQL, parameters, and other DynamicSQL tags.


### 4.2 The &lt;choose> Tag


The CHOOSE tag picks the first nested WHEN tag that evaluates to true and discard the rest. If none is selected and an OTHERWISE tag is declared, then this one will be selected.

The following rules apply to the CHOOSE tag:

- Can include one or more WHEN tags. They take a similar form of the IF tag; that is, it has `test` predicate and has nested content.
- After all the WHEN tags (if any) there can be one OTHERWISE tag. This one doesn't have attributes, but only content.
- Note that the CHOOSE tag cannot directly include SQL content, but only WHEN and OTHERWISE tags.
- The WHEN and OTHERWISE tag can nest static SQL, parameters, and other DynamicSQL tags.

The following example includes a choose operator that implements four types of ordering for the query:


```xml
  <select method="listEmployees" vo="Employee">
    <parameter name="ordering" type="Integer" />
    SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'
    <choose>
      <when test="ordering == 1"> ORDER BY first_name</when>
      <when test="ordering == 2"> ORDER BY last_name</when>
      <when test="ordering == 3"> ORDER BY hired_on DESC</when>
      <otherwise> ORDER BY salary</otherwise>
    </choose>
  </select>
```

### 4.3 The &lt;foreach> Tag

The FOREACH tag iterates over a collection or array of items. In every iteration the current item is available for use in the variable scope. The nested content is included and reevaluared once per each item.

The following example makes it possible to use a list of values in a SQL IN predicate:


```xml
<select method="findEmployees" vo="EmployeeVO">
  <parameter name="ids" type="java.lang.Integer[]" />
  SELECT * FROM employee WHERE id IN
  <foreach item="id" collection="ids" open="(" separator=", " close=")">
    #{id}
  </foreach>
</select>
```

The FOREACH tag defines delimiters to include at the beginning, as separators, and at the end of the content. These parameters are:

- `item`: the name of a variable that will hold the current item
- `collection`: the JEXL expression that will produce an array or collection of items
- `open`: the opening delimiter
- `separator`: the separator to be included by for each between each interation
- `close`: the closing delimiter

In this example the body of the foreach operator includes a single section: `#{id}`. As well as any DynamicSQL tag the body of this operator can include any DynamicSQL tags, SQL sections, or parameters in many nesting levels, as needed.

In the example above, if the array of Integers included 341, 570, and 115, the query would be logged as:

```sql
  SELECT * FROM employee WHERE id IN (?, ?, ?)

  JDBC Parameters (3):
    1. id#0: 341 (java.lang.Integer)
    2. id#1: 570 (java.lang.Integer)
    3. id#2: 115 (java.lang.Integer)
```

**Note**: The FOREACH tag can render long queries when the array or collection includes many values. Keep in mind that there could be a performance penalty in the database when filtering by many values. Also, some database engines and JDBC drivers may place a limit in the size of the SQL statement; most databases will accept a 10000-character long SQL query, but may reject 50000-character long one.


### 4.4 The &lt;bind> Tag

The BIND tag binds a variable in the parameter scope, so it can be used by other content in the rest of the query.


```xml
<select method="findClientsByPartialName" vo="Client">
  <parameter name="partialName" type="String" />
  <bind name="pattern" value="'%' || partialName || '%'" />
  SELECT *
  FROM client
  WHERE name LIKE #{pattern}
</select>
```

In this example `pattern` is not a parameter provided by in the parameter context, but it's a variable defined inside the query. This variable is later used in the query. Variables, as well as parameters, can be applied or injected in the query.

The bind operator does not have a body and, therefore, cannot nest other sections.


### 4.5 The &lt;trim> Tag

The TRIM tag includes multiple IF tags. Each IF tag is evaluated for inclusion and the included ones make it to the query. The TRIM tag joins them with the defined separator.

The example below decides to include or exclude columns in the select list at runtime:

```xml
  <select method="getEmployeeData" vo="EmployeeData">
    <parameter name="fn" type="Boolean" />
    <parameter name="ln" type="Boolean" />
    <parameter name="hd" type="Boolean" />
    SELECT
    <trim separator=", ">
      <if test="fn != null">first_name</if>
      <if test="ln != null">last_name</if>
      <if test="hd != null">hired_on</if>
    </trim>
    FROM employee
  </select>
```

In this example, the columns are included according to the provided boolean parameters, and stitched together using commas (`,`).

Trim uses the following parameters for formatting purposes:

- The `prefix` to prepend when at least one inner fragment is included.
- The `suffix` to append when at least one inner fragment is included.
- The `prefixOverrides` indicates the prefixes to remove from the first selected inner fragment; the list is separated by the `|` (pipe) character.
- The `suffixOverrides` indicates the suffixes to remove from the last selected inner fragment; the list is separated by the `|` (pipe) character.

**Note**: The `<TRIM>` tag cannot directly include SQL content, but only `<if>` tags.

Therefore:

- A `<trim prefix='WHERE' prefixOverrifes="AND|OR">` tag is equivalent to a `<where>` tag.
- A `<trim prefix='SET' prefixOverrifes=",">` tag is equivalent to a `<set>` tag.


### 4.6 The &lt;where> Tag

A WHRE tag is a tailored TRIM tag that simplifies the writing of dynamic WHERE clauses.

For example:

```xml
  <select method="getEmployeeData" vo="EmployeeData">
    <parameter name="fn" type="Boolean" />
    <parameter name="ln" type="Boolean" />
    <parameter name="hd" type="Boolean" />
    SELECT * FROM employee
    <where separator="OR">
      <if test="f.first != null">first_name = #{f.first}</if>
      <if test="f.last != null">last_name = #{f.last}</if>
      <if test="f.hiredDate != null">hired_on = #{f.hiredDate}</if>
    </where>
  </select>
```

In this case the WHERE tag assembles any of these three IF tags prepending `WHERE` to the whole section and adding `OR` between them. If none of them is selected nothing will be added to the query, not even the `WHERE` section.

The default separator is `AND`.

If the supplied parameters are (firstName = `Anne`, lastName = `null`, hiredDate = `2025-03-15`) the query will be assembled as:

```sql
  SELECT * FROM employee
  WHERE first_name = ?
     OR hired_date = '2025-03-15'

  JDBC Parameters (2):
    1. first: Anne (java.lang.String)
    2. hiredDate: 2025-3-15 (java.time.LocalDate)
```

Notice that:
- The `WHERE` clause was included, since at least one inner IF tag was included.
- The `OR` separator was not added to `first_name = ?` since this is the first included tag.
- The `OR` separator was added to `OR hired_on = ?` since this is not the first included tag.
- The second inner tag was not included, since its condition was not met.


### 4.7 The &lt;set> Tag

A SET tag is a tailored TRIM tag that simplifies the writing of dynamic SET clauses.

For example:

```xml
  <query method="markOutstandingInvoices">
    <parameter name="newStatus" type="String" />
    <parameter name="dueDate" type="java.util.Date" />
    UPDATE invoice
    <set>
      <if test="newStatus != null">invoice_status = #{newStatus}</if>
      <if test="dueDate != null">invoice_due_date = #{dueDate}</if>
    </set>
    WHERE total_amount_due > amount_paid
  </query>
```
In this case the SET tag will assemble any if tag prepending `SET`. It will remove the `,` if present in the fist included content. If none of them is selected nothing will be added to the query, not even the `SET` section.

In this example, if the supplied parameters are (newStatus = `null`, dueDate = `2020-12-01`) the query will be assembled as:

```sql
  UPDATE invoice
  SET invoice_due_date = ?
  WHERE total_amount_due > amount_paid

  JDBC Parameters (1):
    1. dueDate: 2020-12-01 (java.time.LocalDate)
```

Notice that:
- The `SET` clause was included, since at least one inner tag was included.
- The `,` in `, invoice_due_date = '2020-12-01'` was removed, since this is the first included tag.
- The first inner tag was not included, since its condition was not met.




