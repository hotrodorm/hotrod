# Combining Collections and Associations

Collections and associations can be combined and nested in the same query. Collections can include nested
collections and associations, while associations can only include nested associations.

When a collection is included, the developer needs to ensure the `ORDER BY` clause is set to ensure the
driving tables are retrieved in the appropriate order.


## Associations and Collections Without Primary Keys

Use the `id` attribute to decide which columns act as a VO key.

```xml
<select method="findSingleExpandedAccount" multiple-rows="false">
  select
  <columns>
    <vo table="log" id="recorded_by, recorded_at" extended-vo="LogWithOfficeVO">
      l.*
      <association view="view_office" id="office_id" property="office" alias="o" />
    </vo>
  </columns>
  from log l
  left join view_office o on l.office_id = o.id
</select>
```

When mutiple columns are needed as an artificial key, a comma-separated value can be specified, as shown in the example.



