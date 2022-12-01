# Combining All Features

All features can be put together in a single graph select.


## Example

The following example combines collections, associations, expressions, with nested collections, associations, 
expressions, artificial keys, and parameters in a single graph query:

```xml
<select method="selectChannels">
  <parameter name="vendorId" java-type="Integer" />
  select
    <columns>
      <vo table="channel" extended-vo="ExtendedChannel" alias="h">
        <association table="client" property="client" alias="c" />
        <association table="service" property="service" alias="s" />
        <collection table="prefix" property="prefixes" alias="p" />
        <expression property="vip">
          case when h.type in ('T1', 'F', 'G') then 1 else 0 end
        </expression>
      </vo>
    </columns>
    from channel h
    join client c on c.id = h.client_id
    join service s on s.id = h.service_id
    join prefix p on p.channel_id = h.id
    where h.vendor_id = #{vendorId}
</select>
```

