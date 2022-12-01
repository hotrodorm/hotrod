# Insert by Example

The `insertByExample()` method is available in views that are based on an underlying table that 
does not have an auto-generated primary key. It receives a VO with properties of the
view row to be inserted. All non-null values are used in the insertion and should correspond to
columns in the underlying driving table.

Consider that it's possible to insert rows through a view, but this depends in the specifics of each
database. The most common rules database impose to do this have to do with two aspects:

- The view must have a non-aggregated driving table.
- The primary key of it should be available as a column in the view.
- If the view has a filtering condition, the database may enforce the rule that the inserted row 
must be valid according to it.
- The underlying driving table should not implement primary key auto-generation logic such as `IDENTITY`.


## Example

The following table and view are created (in PostgreSQL):

```sql
create table client (
  id int primary key,
  name varchar(20),
  active boolean
);

create view active_client as
select * from client where active;
```

The app could insert a row through the view with:

```java
@Autowired
private ActiveClientDAO activeClientDAO;

...

ActiveClientVO c = new ActiveClientVO();
c.setId(123);
c.setName("Julie Andrews");
c.setActive(true);
this.activeClientDAO.insertByExample(c);
```

The new row is now inserted in the `client` table trough the `active_client` view.





