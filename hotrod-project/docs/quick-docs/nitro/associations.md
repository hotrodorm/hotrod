# Associations

An `<association>` tag is a special kind of `<vo>` tag. Its represents a `<vo>` tag that has a 1:1 relationship with its parent `<vo>` tag. Thus, it does not require a property of type `List<VO>`, but only of type `VO`. 

It's not required for the *associated* values to be present. An outer join may produce no values related to the driving row. In this case the whole associated VO is null.

It's responsibility of the developer to ensure at most a single associated value is produced by the query. If more than one is produces the behavior is not deterministic, and it will most proably only retrieve the last value.

Associations can also be nested, typically when traversing joins to retrieve a parent row, or a parent of a parent row, and so on.

The use of an `<association>` tag does not require any particular order in the result set.


## Example

The following query select a car VO, where each car VO includes a single owner VO as an embedded property:

```xml
<dao name="NightlyQueriesDAO">

  <select method="findCarsWithOwners">
    select
      <columns>
        <vo table="car" extended-vo="CarWithOwnerVO">
          p.*
          <association table="owner" property="carOwner" alias="o" />
        </vo>
      </columns>
    from car c
    left join owner o on c.owner_id = o.id
  </select>
  
</dao>
```

In this example:

 - Since we are probably navigating a foreign key a car can have at most one owner.
 - The query returns a `List<CarWithOwnerVO>`.
 - The VO `CarWithOwnerVO` extends `CarVO`.
 - The `carOwner` property of the class `CarWithOwnerVO` is of type `OwnerVO`.
 - Since the query uses a `LEFT JOIN` this will include cars without owner. That means the property `carOwner` may be null for some rows.

![](images/structured-select6.png)
