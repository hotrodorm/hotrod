# The SELECT List

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

## Variations

LiveSQL includes variations to specify all or a subset of the columns and also to qualify the query for DISTINCT rows only. See the variations below.


### Select All Columns

To select all columns of the table(s) start the query with:

```java
this.sql.select()
```

The resulting SQL statement starts with:

```sql
select *
```

### Select Specific Columns

To select a subset of the columns of the table(s) start the query with:

```java
this.sql.select(a.name, a.price.mult(a.qty).as("total"), a.status)
```

Produces:

```sql
select a.name, a.price * a.qty as total, a.status
```

The query can name the specific list of columns to produce. This list can also include expressions that the database can 
compute using functions or operators. 

### Select Distinct Rows

The query can indicate that only distinct rows should be generated, as shown below:

```java
this.sql.selectDistinct()
```

Produces:

```sql
select distinct *
```

### Select Distinct Rows

The DISTINCT qualifier can also be combined with specific columns:

```java
this.sql.selectDistinct(a.name, a.price.mult(a.qty).as("total"), a.status)
```

Produces:

```sql
select distinct a.name, a.price * a.qty as total, a.status
```
