# Select by Primary Key

The `selectByPK()` method is implemented in DAOs related to a table that has a primary key.

The method receives as parameter all primary key columns and returns a VO with the selected row.


## Example

In the example shown below the first table has a single-column primary key, the second table
has a composite primary key and the third one doesn't have a primary key:

```sql
create table phone (
  id bigint primary key not null,
  phone_number varchar(12)
);

create table recipe_ingredient (
  recipe_id int not null,
  ingredient_id varchar(16) not null,
  quantity number(12,2),
  primary key (recipe_id, ingredient_id)
);

create table audit_log (
  recorded_at timestamp not null,
  message varchar(80) not null
);
```

CRUD will produce one DAOs for each table:
- `PhoneDAO` will include the method `PhoneVO selectByPK(Long id)`.
- `RecipeIngredientDAO` will include the method `RecipeIngredientVO selectByPK(Integer recipeId, String ingredientId)`.
- `AuditLogDAO` will not include a `selectByPK()` method.

