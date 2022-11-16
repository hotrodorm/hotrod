# Single VO per Row

Sometimes the resulting rows of a structured VO happens to coincide exactly with an existing VO. In this case
there's no need to define a new VO, but the existing one can be reused. 

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

The method `retrieveActiveAccounts` does not define new VOs, but reuses the existing VO and returns
a `List<AccountVO>`. This VO includes all custom behavior the developer may have added.

The same rules shown in the previous case apply. Not all columns need to be retrieved, and parameters can be applied.

The "Single VO return" must be read as "columns belonging to a single VO", not as "returning a single row". 
Structured Selects are assumed to return multiple rows in a list structure.

