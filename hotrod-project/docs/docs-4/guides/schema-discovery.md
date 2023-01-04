# Schema Discovery

HotRod includes a mechanism to discover tables and views in schemas of the database. These discovered tables and views 
are added to the persistent layer automatically.

Discovery is enabled by adding the `<discovery>` tag inside the `<mybatis-spring>` tag.

It's also possible to combine discovery with declared tables and views. The persistence layer generation honors the
details of declared tables and views.


## Examples

The examples below show different cases of schema discovery.


### Example #1 - Discovery Disabled by Default

Discovery is disabled by default. The configuration declares a table, a view, and a DAO.

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client" />
  <view name="outstanding_payments" />
  <dao name="ProcessesDAO">
    <query method="cleanUpTempData">truncate table temp_data</query>
  </dao>

</hotrod>
```

Only the table `client`, the view `outstanding_payments`, and the DAO `ProcessesDAO` are included in
the persistence layer. No discovery of tables or views takes place.


### Example #2 - Discovery Enabled for the Current Schema

Discovery is enabled. The configuration does not declare any table, view, or DAO, so all tables and views
in the current schema will be discovered and added to the persistence layer.

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery />
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

</hotrod>
```

All tables and views in the current schema are discovered and added to the persistence layer. They are available
for CRUD and LiveSQL.


### Example #3 - Discovery and Declared Tables or Views

Discovery is enabled. The configuration declares a table, a view, and a DAO.

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery />
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />
  <dao name="ProcessesDAO">
    <query method="cleanUpTempData">truncate table temp_data</query>
  </dao>

</hotrod>
```

Since discovery is enabled, all tables and views in the current schema are included in the persistence
layer. The custom configuration details for the declared tables and views are honored.

Tables and view &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #4 - Excluding Tables or Views from Discovery

The configuration specifies tables and views that we want to exclude from the discovery:

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery>
        <exclude name="invoice_bkp_tab" />
        <exclude name="accounting_old_view" />
      </discovery>
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

Any tables or views in the default schema are included in the persistence layer, except for `invoice_bkp_tab` and
`accounting_old_view`. The declared table and view (`client_tab` and `outst_payments`) are included in the
persistence layer.


### Example #5 - Discovering Multiple Schemas

The configuration includes a list of comma-separated schemas to be discovered:

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery schemas="accounting, billing" />
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

Any tables or views in the schemas `accounting` and `billing` are included in the persistence layer. The declared
table and view (`client_tab` and `outst_payments`) are also included in the persistence layer.


### Example #6 - Excluding Tables and View from Multiple Schemas

The configuration includes a list of comma-separated schemas to be discovered and also some tables or views
to be excluded from discovery.

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery schemas="accounting, billing">
        <exclude schema="accounting" name="invoice_bkp_tab" />
        <exclude schema="billing" name="accounting_old_view" />
      </discovery>
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

Any tables or views in the schemas `accounting` and `billing` are included in the persistence layer except
for `accounting.invoice_bkp_tab` and `billing.accounting_old_view`. The declared table and view
(`client_tab` and `outst_payments`) are also included in the persistence layer.


### Example #7 - Catalogs and Schemas

In databases that implement catalogs and schemas (SQL Server and Sybase), these can be included using dot notation.

```xml
<hotrod>

  <generators>
    <mybatis-spring>
      <discovery schemas="master.accounting, clients.billing">
        <exclude catalog="master" schema="accounting" name="invoice_bkp_tab" />
        <exclude catalog="clients" schema="billing" name="accounting_old_view" />
      </discovery>
      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

Any tables or views in the schemas `master.accounting` and `clients.billing` are included in the persistence layer except
for `master.accounting.invoice_bkp_tab` and `clients.billing.accounting_old_view`. The declared table and view
(`client_tab` and `outst_payments`) are also included in the persistence layer.



## See also

For details see:
- The [`Configuration File Reference`](../config/README.md)
- The [`<mybatis-spring>`](../config/tags/mybatis-spring.md) tag
- The [`<exclude>`](../config/tags/exclude.md) tag


