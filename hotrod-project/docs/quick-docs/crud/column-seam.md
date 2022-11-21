# Column Seam

Since CRUD generates the DAO methods automatically from the tables metadata, it
may be possible in some rare ocassions that two or more generated methods can end up
having the exact same name.

Since this produces an invalid Java class, CRUD includes a *column seam* that can
forces a differentiation between them. By default this seam is an empty string, but 
may be configured to have a different value. See [Table](../config/tags/table.md) for 
details on how to configure it.

Name collissions can theoretically affect the following CRUD methods in DAOs:
- [Select by Unique Index](./select-by-unique-index.md)
- [Select Parent by Foreign Key](./select-parent-by-foreign-key.md)
- [Select Children by Foreign Key](./select-children-by-foreign-key.md)


## Example

The following carefully-crafted table will produce a name collision when generating
multiple `selectByUI...()` methods:

```sql
create table clothing (
  id int primary key not null,
  brand int,
  part_type int,
  brand_part int,
  type int
);

create unique index ix1 on clothing (brand, part_type);

create unique index ix2 on clothing (brand_part, type);
```

With the default seam (an empty string) CRUD generates two DAO methods with the
same name, something that could result in an invalid Java class:
- `ClothingVO selectByUIBrandPartType()` method for the first index (`ix1`).
- `ClothingVO selectByUIBrandPartType()` method for the second index (`ix2`).

To resolve the name collision we can define a `column-seam` in this table as 
an underscore (`_`). The DAO will now have two methods with [slightly] different names:
- `ClothingVO selectByUIBrand_PartType()` for the first index `ix1`.
- `ClothingVO selectByUIBrandPart_Type()` for the second index `ix2`.

Adding the column seam produces a perflectly valid Java class for the DAO.




