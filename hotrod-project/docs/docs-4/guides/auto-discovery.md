# Auto-Discovering Tables &amp; Views

HotRod discovers tables and views and adds them to the persistent layer automatically when no `<table>`, `<view>`, or `<dao>` tags are 
present in the configuration.

This feature can also be enabled if any of these tags are present to combine auto-discovery with tables and views 
configured in an explicit way.


## Example #1 - Auto-discovery enabled automatically

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

Auto-discovery is activated by default, and all tables and view in the default schema are included in the persistence layer.


## Example #2 - Auto-discovery disabled automatically

The configuration shown below does not include any table, view or DAO:

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

</hotrod>
```

Auto-discovery is disabled since at least one table or view are specified in the configuration. Only these tables and views are included in the persistence layer.


## Example #3 - Combining auto-discovery with explicitly configured tables or views

The configuration shown below does not include any table, view or DAO:

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

</hotrod>
```

Auto-discovery is explicitly enabled in the configuration. All tables and view in the default schema are included in the persistence
layer. The extra configuration details for tables and views are honored and used in the persistence layer.



## Example #4 - Excluding Tables or Views from Auto-Discovery

The configuration shown below does not include any table, view or DAO:

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

Tables and views can be excluded from the auto-discovery feature by using the `exclude` tag.  


## See also

For details see:
- The [`<mybatis-spring>`](tags/mybatis-spring.md) tag
- The [`<exclude>`](tags/exclude.md) tag


