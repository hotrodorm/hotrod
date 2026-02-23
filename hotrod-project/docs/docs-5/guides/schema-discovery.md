# Schema Discovery

HotRod includes a mechanism to discover tables and views in schemas of the database. These discovered tables and views are added to the persistent layer automatically.

Schema discovery can help with quick protoyping in the early stages of a project.

In No-Config mode &mdash; when no layer configuration file is specified &mdash; schema discovery is automatically selected. It finds and add all the tables and views in the current schema to the persistence layer.

When you specify the layer configuration file, schema discovery is turned off by default and needs to be turned on manually. Once activated, it can be configured to discover one or more schemas.

Schema discovery plays along well with tables and view defined in the layer configuration explicitly. The declared configuration of tables and view supersedes the default ones. This way the tables and view not explicitly declared can still use the default dialect rule, while the explicit one use the declared rules.

By combining the static type solver, runtime type solver, and name solver, schema discovery can create a fully functional persistence layer with very little setup, even for complex schemas or database types.

Schema Discovery is mutually exclusive with facets. If you want to use discovery you cannot define facets, and vice versa.

## See Also

Related configuration and articles:

- [&lt;discover>](../config/tags/discover.md)
- [&lt;current-schema>](../config/tags/current-schema.md)
- [&lt;schema>](../config/tags/schema.md)
- [&lt;exclude>](../config/tags/exclude.md)
- The [Configuration File Reference](../config/README.md)


## Examples

The examples below show different cases of schema discovery.

### Example #1 - No-Config Mode

If the layer configuration file is not specified the schema discover is automatically enable include the current schema.

This is equivalent to using the following layer configuration file:

```xml
<hotrod>

  <generators>
    <jdbc>
      <discover />
    </jdbc>
  </generators>

</hotrod>
```

**Note**: the current schema is the schema specified by the `catalog` and `schema` when using HotRod's generation through the plugins. This name of the current schema is always defined externally, not in the layer configuration file.


### Example #2 - Discovery Disabled by Default

When a layer configuration file is used Schema Discovery is disabled by default. The following *explicit* layer configuration declares a table, a view, and a DAO.

```xml
<hotrod>

  <generators>
    <jdbc/>
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


### Example #3 - Enlabing Discovery in the Current Schema

We can enable Schema Discovery by adding the &lt;discover> tag. This will automatically include the current schema.

```xml
<hotrod>

  <generators>
    <jdbc>
      <discover />
    </jdbc>
  </generators>

</hotrod>
```

All tables and views in the current schema are discovered and added to the persistence layer. They are available for CRUD and LiveSQL.

As indicated above, an empty `<discover/>` tag includes the current schema by default. In this case, the example above is equivalent to:

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover>
        <current-schema />
      </discover>

    </jdbc>
  </generators>

</hotrod>
```


### Example #4 - Discovery with Declared Tables and Views

In this case Schema Discovery is enabled and some tables are views are also declated. In this case, the layer configuration file declares one table, one view, and one DAO.

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover />

    </jdbc>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />
  <dao name="ProcessesDAO">
    <query method="cleanUpTempData">truncate table temp_data</query>
  </dao>

</hotrod>
```

Since discovery is enabled, all tables and views in the current schema are included in the persistence
layer. The custom configuration details for the declared tables and views supersedes the discovery functionality.

In short, all tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #5 - Discovering Multiple Schemas

The configuration indicates that the current schema, as well as two extra ones will be discovered.
There are also declared tables, views, and DAOs:

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover>
        <current-schema />
        <schema name="sales" />
        <schema name="payments" />
      </discover>

    </jdbc>
  </generators>

  <table name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />
  <dao name="ProcessesDAO">
    <query method="cleanUpTempData">truncate table temp_data</query>
  </dao>

</hotrod>
```

All tables and views in the current schema are included in the persistence layer. The declared table and view (`client_tab` and `outst_payments`) are included in the persistence layer with their custom
configuration.

All tables in the `sales` and `payments` schemas are also included in the persistence layer.

In short, all tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.

**Note**: when schemas are added using `<schema>` tags, then the current schema that is otherwise included by default is not included anymore. If you want to include it nevertheless, add the `<current-schema>` tag.

### Example #6 - Excluding Tables and Views from Discovery

The configuration specifies tables and views that we want to exclude from the discovery:

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover>
        <current-schema>
          <exclude name="invoice_bkp_tab" />
          <exclude name="past_due_invoice_3" />
        </current-schema>
        <schema name="accounting">
          <exclude name="accounting_old_view" />
        </schema>
      </discover>

    </jdbc>
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


### Example #7 - Catalogs and Schemas

In databases that implement catalogs and schemas &ndash; e.g. SQL Server and Sybase &ndash; these can 
be included by adding the `catalog` attribute when necessary.

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover>
        <schema name="reporting" />
        <schema catalog="master" name="accounting">
          <exclude name="invoice_bkp_tab" />
        </schema>
        <schema catalog="clients" name="billing">
          <exclude name="accounting_old_view" />
        </schema>
      </discover>

    </jdbc>
  </generators>

  <table catalog="master" schema="accounting" name="client_tab" sequence="seq_client" />
  <view name="outst_payments" java-name="OutstandingPayment" />

</hotrod>
```

All tables and views in the schema `reporting` of the current catalog are in include in the persistence layer. Also, all tables and views of the schemas `master.accounting` and `clients.billing` are included in the persistence layer except for `master.accounting.invoice_bkp_tab` and `clients.billing.accounting_old_view`.

The declared table `master.accounting.client_tab` and the view `outst_payments` in the current schema are also
included in the persistence layer with their custom settings.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


### Example #8 - Catalogs Without Schemas

In databases that implement catalogs but do not implement schemas &ndash; e.g. MariaDB and MySQL &ndash; these can be discovered by using the `catalog` attribute. Catalogs are typìcally knowns as *databases* by theses engines.

```xml
<hotrod>

  <generators>
    <jdbc>

      <discover>
        <schema catalog="marketing" />
        <schema catalog="insurance">
          <exclude name="mortality_table" />
        </schema>
        <schema catalog="sales">
          <exclude name="report_bkp" />
        </schema>
      </discover>

    </jdbc>
  </generators>

</hotrod>
```

All tables and views in the catalog/database `marketing` are in included in the persistence layer.
Also, all tables and views of the catalog/database `insurance` and `sales` are included in the persistence layer except for `insurance.mortality_table` and `sales.report_bkp`.

All tables and views &ndash; either discovered or declared &ndash; are available for CRUD and LiveSQL.


