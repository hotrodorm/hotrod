# The DISTINCT ON Clause

The DISTINCT ON clause affects the execution of a SELECT query by only returning the first row of each group. 

DISTINCT ON is available since 4.6.


## Description

The grouping is done according to a list of expressions specified between parenthesis. 

The ORDER BY clause is mandatory for these queries and must start with the same expressions that the DISTINCT ON clause includes. The ORDER BY clause can include 
more ordering terms.

Notes:

- The DISTINCT ON clause is not compatible with the standard DISTINCT clause. Only one of them can be used in the SELECT list.
- A SELECT query with a DISTINCT ON clause cannot lock rows.
- Not only columns can be used in the DISTINCT ON clause, but virtually any comparable expression.
- The DISTINCT ON clause can include multiple expressions. All of them are used to compare and discard rows.

DISTINCT ON is only implemented by the PostgreSQL database.


## Examples

For example, the following query will return the highest paid employee of each department.

```
select distinct on (department_id) *
from employee
order by department_id, salary desc
```

It's written in LiveSQL as:
 
```java
EmployeeTable e = EmployeeDAO.newTable("e");

List<Row> rows = this.sql
    .selectDistinctOn(e.departmentId)
    .from(e)
    .orderBy(e.departmentId, salary.desc()) 
    .execute();
```

Any comparable expression can be used in the DISTINCT ON clause. The following query returns
the invoices with the highest unpaid balances on each branch and type:

```java
InvoiceTable i = InvoiceDAO.newTable("i");

List<Row> rows = this.sql
    .selectDistinctOn(i.branchId.plus(sql.literal(100), i.type)) //
    .columns(i.star(), i.unpaidBalance.mult(i.interestRate).as("extra"))
    .from(i) //
    .orderBy(i.branchId.plus(sql.literal(100)), i.type, i.unpaidBalance.desc());
```

To produce a SQL query like:

```ql
select
  distinct on (i.branch_id + 100, i.type)
  i.*,
  i.unpaid_balance * i.interestRate as "extra"
from invoice i
order by i.branch_id + 100, i.type, i.unpaid_balance desc
```

**Note**: Again, notice that the ORDER BY clause starts with the same expressions indicated in the DISTINCT ON clause. This is mandatory.

