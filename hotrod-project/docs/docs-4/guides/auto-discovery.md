# Auto-Discovering Tables &amp; Views

HotRod includes a mechanism to discover tables and views in the current schema. It adds them to the persistent layer automatically when no `<table>`, `<view>`, or `<dao>` tags are 
present in the configuration.

This feature can also be enabled in the `<mybatis-spring>` tag to combine the auto-discovery
with explicit configuration of tables and views.


## Examples

The examples below show different cases of auto-discovery.


### Example #1 - Auto-Discovery Enabled Automatically

The configuration shown below does not include any table, view or DAO:

```xml
<hotrod>

  <generators>
    <mybatis-spring >
      <daos package="com.myapp.daos" dao-suffix="DAO" vo-suffix="Impl" 
            abstract-vo-prefix="" abstract-vo-suffix="VO" />
    </mybatis-spring>
  </generators>

</hotrod>
```

Auto-discovery is activated by default. All tables and views in the default schema are included in the persistence layer.


### Example #2 - Auto-Discovery Disabled Automatically

The configuration shown below **does include** one table table, view or DAO:

```xml
<hotrod>

  <generators>
    <mybatis-spring >
      <daos package="com.myapp.daos" dao-suffix="DAO" vo-suffix="Impl" 
            abstract-vo-prefix="" abstract-vo-suffix="VO" />
    </mybatis-spring>
  </generators>

  <table name="client" />
  <view name="outstanding_payments" />
  <dao name="ProcessesDAO">
    <query method="cleanUpTempData">truncate table temp_data</query>
  </dao>

</hotrod>
```

Auto-discovery is disabled since at least one table, view or DAO are specified in the configuration. Only these tables, views and DAOs are included in the persistence layer.


### Example #3 - Combining auto-discovery with explicitly configured tables or views

The configuration shown below **does include** a table, view or DAO, and auto-discovery is also explicitly enabled:

```xml
<hotrod>

  <generators>
    <mybatis-spring discovery="enabled" schemas="">
      <daos package="com.myapp.daos" dao-suffix="DAO" vo-suffix="Impl" 
            abstract-vo-prefix="" abstract-vo-suffix="VO" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

Auto-discovery is explicitly enabled in the configuration. All tables and view in the default schema are included in the persistence
layer. The custom configuration details for tables and views are honored and are used in the persistence layer.


### Example #4 - Excluding Tables or Views from Auto-Discovery

The configuration shown below specifies tables or views that we want to exclude from the auto-discovery:

```xml
<hotrod>

  <generators>
    <mybatis-spring auto-discovery="enabled">
      <daos package="com.myapp.daos" dao-suffix="DAO" vo-suffix="Impl" 
            abstract-vo-prefix="" abstract-vo-suffix="VO" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

  <exclude name="invoice_bkp_tab" />
  <exclude name="accounting_old_view" />

</hotrod>
```

Any tables or views in the default schema are included in the persistence layer, except for `invoice_bkp_tab` and `accounting_old_view`.
`client_tab` and `outst_payments` are included with their custom configuration.


## See also

For details see:
- The [`Configuration File Reference`](../config/README.md)
- The [`<mybatis-spring>`](../config/tags/mybatis-spring.md) tag
- The [`<exclude>`](../config/tags/exclude.md) tag


