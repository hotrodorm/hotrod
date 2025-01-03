# Single VO per Row

Sometimes the resulting columns of a graph select happens to coincide exactly with the structure of 
an existing VO. In this case there's no need to define a new VO, but the existing one can be reused. 

In this case the existing VO and all its custom behavior is fully reused. See
[Value Object Modeling](../crud/value-object-modeling.md) for details.


## Example

If the `<columns>` tag includes a single VO, then there's no need to create an new enclosing VO and
all columns can be returned under this existing VO. For example:

```xml
<dao name="NightlyQueriesDAO">

  <select method="retrieveActiveAccounts">
    select
      <columns>
        <vo table="account" alias="a" />
      </columns>
    from account a
    where a.active = 1
  </select>
  
</dao>
```

The method `retrieveActiveAccounts` does not define a new VO, but reuses the existing one and returns
a `List<AccountVO>`. This VO includes all custom behavior the developer may have added.

The general rules of graph selects apply. Parameters can be applied, Native SQL and Dynamic SQL can be used.

Additionally, sometimes for performance reasons only a subset of columns can be retrived by adding a body 
to the tags. For example, changing one line in the query above will retrieve and populate only two columns
of the VO:

```
        <vo table="account" alias="a">a.id, a.balance</vo>
```

