# Schema Discovery

HotRod includes a mechanism to discover tables and views in schemas of the database. These discovered tables and views 
are added to the persistent layer automatically.

Discovery is enabled by adding the `<discover>` tag inside the `<mybatis-spring>` tag.

Discovery is mutually exclusive with facets. If you want to use discovery you cannot define facets, and vice versa.

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
the persistence layer. Since the tag `<discover>` is not included, no discovery of tables or views 
takes place.


### Example #2 - Discovery Enabled for the Current Schema

Discovery is enabled. All tables and views in the current schema will be discovered and added to 
the persistence layer.

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover />

      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

</hotrod>
```

All tables and views in the current schema are discovered and added to the persistence layer. They are available
for CRUD and LiveSQL.

An empty `<discover/>` tag includes the current schema by default. In this case, the example above is equivalent to:

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover>
        <current-schema />
      </discover>

      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

</hotrod>
```


### Example #3 - Discovery with Declared Tables and Views

Discovery is enabled. The configuration declares a table, a view, and a DAO.

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover />

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
layer. The custom configuration details for the declared tables and views supersedes the discovery 
functionality.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #4 - Specifying Schemas to Discover

The configuration indicates that the current schema, as well as two extra ones will be discovered.
There are also declared tables, views, and DAOs:

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover>
        <current-schema />
        <schema name="sales" />
        <schema name="payments" />
      </discover>

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

All tables and views in the current schema are included in the persistence layer. The declared table and
view (`client_tab` and `outst_payments`) are included in the persistence layer with their custom
configuration.

All tables in the `sales` and `payments` schemas are also included in the persistence layer.

**Note**: When the `<discover>` tag is empty and no schema is declared, then the current schema is
included by default; the current schema is the schema specified by the `catalog` and `schema` runtime
properties that are configured in the external configuration, and not in the main configuration file.
If schemas are added using `<schema>` tags, then the current schema is not included by default anymore.
If you want to include it nevertheless, add the `<current-schema>` tag.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #5 - Excluding Tables and Views from Discovery

The configuration specifies tables and views that we want to exclude from the discovery:

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover>
        <current-schema>
          <exclude name="invoice_bkp_tab" />
          <exclude name="past_due_invoice_3" />
        </current-schema>
        <schema name="accounting">
          <exclude name="accounting_old_view" />
        </schema>
      </discover>

      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

All tables and views in the current schema are included in the persistence layer, except for the tables (or views)
that match the names `invoice_bkp_tab` and `past_due_invoice_3`. All tables and views in the schema `accounting` 
are also included in the persistence layer, except for `accounting_old_view`.

The declared table and view (`client_tab` and `outst_payments`) are included in the persistence layer
with their custom settings.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #6 - Catalogs and Schemas

In databases that implement catalogs and schemas &ndash; e.g. SQL Server and Sybase &ndash; these can 
be included by adding the `catalog` attribute when necessary.

```xml
<hotrod>

  <generators>
    <mybatis-spring>

      <discover>
        <schema name="reporting" />
        <schema catalog="master" name="accounting">
          <exclude name="invoice_bkp_tab" />
        </schema>
        <schema catalog="clients" name="billing">
          <exclude name="accounting_old_view" />
        </schema>
      </discover>

      <daos package="app.persistence" />
    </mybatis-spring>
  </generators>

  <table catalog="master" schema="accounting" name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

All tables and views in the schema `reporting` of the current catalog are in include in the persistence layer.
Also, all tables and views of the schemas `master.accounting` and `clients.billing` are included in the persistence 
layer except for `master.accounting.invoice_bkp_tab` and `clients.billing.accounting_old_view`. 

The declared table `master.accounting.client_tab` and the view `outst_payments` in the current schema are also
included in the persistence layer with their custom settings.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


## See also

For details see:
- The [`Configuration File Reference`](../config/README.md)
- The [`<mybatis-spring>`](../config/tags/mybatis-spring.md) tag
- The [`<exclude>`](../config/tags/exclude.md) tag


