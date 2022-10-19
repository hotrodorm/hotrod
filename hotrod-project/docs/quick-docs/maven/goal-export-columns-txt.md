# Exporting Column Metadata to a TXT File

The database exposes a set of properties for each column. These properties can be
used to assemble rules in the `<type-solver>` tag that may help to greatly reduce the configuration effort.

Column metadata can also be exported in XLSX format. See [Export Columns to XLSX](./goal-export-columns-xlsx.md).

## Configuring the Metadata Report Generation

In order to produce this metadata report its name needs to be configured in the HotRod configuration as the `txtexportfile` property.
For example, in the Maven plugin configuration, the developer can configure it as:

```xml
<plugin>
  <groupId>org.hotrodorm.hotrod</groupId>
  <artifactId>hotrod-maven-plugin</artifactId>
  <version>${hotrod.version}</version>
  <configuration>
    <localproperties>dev.properties</localproperties>
    <txtexportfile>data/export-columns.txt</txtexportfile>
    <xlsxexportfile>data/export-columns.xlsx</xlsxexportfile>
  </configuration>
  <dependencies>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.2.5.jre6</version>
      <type>jar</type>
    </dependency>
  </dependencies>
</plugin>
```

The section `<txtexportfile>data/export-columns.txt</txtexportfile>` defines the output location of the TXT columns metadata report.

## Producing the Report

To produce the metadata report use the Maven goal `export-columns-txt`. Once HotRod's Maven plugin is configured as shown above, run:

```bash
mvn hotrod:export-columns-txt
```

Maven will show an output similar to:

```bash
[INFO] HotRod version 3.3.0 - Export Columns TXT
[INFO] Loading local properties from: ...
[INFO]  
[INFO] Configuration File: ...
[INFO] HotRod Database Adapter: PostgreSQL Adapter
[INFO] Database URL: ...
[INFO] Database Name: PostgreSQL - version 12.4 (12.4 (Debian 12.4-1.pgdg90+1))
[INFO] JDBC Driver: PostgreSQL JDBC Driver - version 42.2 (42.2.5.jre6) - implements JDBC Specification 4.0
[INFO]  
[INFO] Default Database Schema: public
[INFO]  
[INFO] Column export saved to: data/export-columns.txt
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

And the report will now be ready at `data/export-columns.txt`.

## Column Metadata Properties

Column metadata properties are divided in two categories:

 - *standard metadata properties*, and
 - *native metadata properties*.

### Standard Metadata Properties

Standard Properties are present in all databases and have standard, well-know names. These properties are provided 
by the JDBC driver or as extra information added by HotRod.

Though they are always available, they only model common aspects of columns metadata. Database-specific features are not covered by these properties; if the developer needs to access the latter, they may need to use the non-standard *native properties*.

The Standard Properties include the following properties provided by the JDBC driver:

 - `catalog`
 - `schema`
 - `objectName`
 - `ordinal`
 - `name`
 - `typeName`
 - `dataType`
 - `size`
 - `scale`
 - `default`
 
In addition to the list above, the Standard Properties also include the following properties provided by HotRod:
 
 - `autogeneration`
 - `belongsToPK`
 - `isVersionControlColumn`
 - `nature`
 - `nullable`

### Native Metadata Properties

Most databases provides extra metadata for their columns. HotRod retrieves this extra metadata and provides them as non-standard properties with the `native.` prefix.

**Note**: Apache Derby users: it seems Derby does not provide native properties. Thus, the section for native properties may be empty. 

Again, these properties change from database to database, and may or may not be available. Though uncommon, their names, values, and types may change between versions of a database.

As an example the PostgreSQL 12 database offers the following native properties:

 - `native.scope_schema`
 - `native.is_nullable`
 - `native.column_default`
 - `native.is_updatable`
 - `native.maximum_cardinality`
 - `native.identity_minimum`
 - `native.numeric_scale`
 - `native.is_self_referencing`  
 - `native.table_catalog`  
 - `native.table_name`  
 - `native.scope_name`  
 - `native.is_generated`  
 - `native.collation_catalog`  
 - `native.domain_name`  
 - `native.character_set_name`  
 - `native.identity_generation`  
 - `native.identity_cycle`  
 - `native.interval_type`  
 - `native.datetime_precision`  
 - `native.collation_schema`  
 - `native.scope_catalog`  
 - `native.column_name`  
 - `native.character_maximum_length`  
 - `native.generation_expression`  
 - `native.collation_name`  
 - `native.is_identity`  
 - `native.character_set_catalog`  
 - `native.domain_catalog`  
 - `native.interval_precision`  
 - `native.character_set_schema`  
 - `native.identity_increment`  
 - `native.domain_schema`  
 - `native.ordinal_position`
 - `native.identity_maximum` 
 - `native.table_schema`
 - `native.data_type`
 - `native.udt_catalog`
 - `native.numeric_precision`  
 - `native.dtd_identifier`
 - `native.character_octet_length`  
 - `native.identity_start`  
 - `native.udt_name`
 - `native.udt_schema`


# Example

The following report was produced from a small example PostgreSQL database with three tables:

```log
HotRod Column Export
--------------------

  From live database at : jdbc:postgresql://db.example.com:5432/hotrod
  Configuration file    : src/database/postgresql-12.4/hotrod.xml
  Catalog               : 
  Schema                : public
  Exported              : 30 Oct 2020 at 09:00:49 -0400
  Generated by          : HotRod version 3.3.0

catalog  schema  objectName      ordinal  name             typeName   dataType        size  scale  default                                      autogeneration       belongsToPK  isVersionControlColumn  nature  nullable  native.numeric_precision_radix  native.scope_schema  native.is_nullable  native.column_default                        native.is_updatable  native.maximum_cardinality  native.identity_minimum  native.numeric_scale  native.is_self_referencing  native.table_catalog  native.table_name  native.scope_name  native.is_generated  native.collation_catalog  native.domain_name  native.character_set_name  native.identity_generation  native.identity_cycle  native.interval_type  native.datetime_precision  native.collation_schema  native.scope_catalog  native.column_name  native.character_maximum_length  native.generation_expression  native.collation_name  native.is_identity  native.character_set_catalog  native.domain_catalog  native.interval_precision  native.character_set_schema  native.identity_increment  native.domain_schema  native.ordinal_position  native.identity_maximum  native.table_schema  native.data_type             native.udt_catalog  native.numeric_precision  native.dtd_identifier  native.character_octet_length  native.identity_start  native.udt_name  native.udt_schema
-------  ------  --------------  -------  ---------------  ---------  --------  ----------  -----  -------------------------------------------  -------------------  -----------  ----------------------  ------  --------  ------------------------------  -------------------  ------------------  -------------------------------------------  -------------------  --------------------------  -----------------------  --------------------  --------------------------  --------------------  -----------------  -----------------  -------------------  ------------------------  ------------------  -------------------------  --------------------------  ---------------------  --------------------  -------------------------  -----------------------  --------------------  ------------------  -------------------------------  ----------------------------  ---------------------  ------------------  ----------------------------  ---------------------  -------------------------  ---------------------------  -------------------------  --------------------  -----------------------  -----------------------  -------------------  ---------------------------  ------------------  ------------------------  ---------------------  -----------------------------  ---------------------  ---------------  -----------------
          public  account               1  id               serial     4                 10      0  nextval('account_id_seq'::regclass)          IDENTITY_BY_DEFAULT  true         false                           false     2                                                    NO                  nextval('account_id_seq'::regclass)          YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           0                                                                         id                  0                                                                                     NO                                                                       0                                                                                                        1                                                 public               integer                      hotrod              32                        1                      0                                                     int4             pg_catalog       
          public  account               2  name             varchar    12               100      0                                                                    false        false                           false     0                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           0                                                                         name                100                                                                                   NO                                                                       0                                                                                                        2                                                 public               character varying            hotrod              0                         2                      400                                                   varchar          pg_catalog       
          public  account               3  type             varchar    12                10      0                                                                    false        false                           false     0                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           0                                                                         type                10                                                                                    NO                                                                       0                                                                                                        3                                                 public               character varying            hotrod              0                         3                      40                                                    varchar          pg_catalog       
          public  account               4  current_balance  int4       4                 10      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           0                                                                         current_balance     0                                                                                     NO                                                                       0                                                                                                        4                                                 public               integer                      hotrod              32                        4                      0                                                     int4             pg_catalog       
          public  account               5  created_on       timestamp  93                29      6                                                                    false        false                           false     0                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           6                                                                         created_on          0                                                                                     NO                                                                       0                                                                                                        5                                                 public               timestamp without time zone  hotrod              0                         5                      0                                                     timestamp        pg_catalog       
          public  account               6  active           int4       4                 10      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                account                               NEVER                                                                                                                     NO                                           0                                                                         active              0                                                                                     NO                                                                       0                                                                                                        6                                                 public               integer                      hotrod              32                        6                      0                                                     int4             pg_catalog       
          public  product               1  id               int8       -5                19      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                product                               NEVER                                                                                                                     NO                                           0                                                                         id                  0                                                                                     NO                                                                       0                                                                                                        1                                                 public               bigint                       hotrod              64                        1                      0                                                     int8             pg_catalog       
          public  product               2  name             varchar    12                20      0                                                                    false        false                           false     0                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                product                               NEVER                                                                                                                     NO                                           0                                                                         name                20                                                                                    NO                                                                       0                                                                                                        2                                                 public               character varying            hotrod              0                         2                      80                                                    varchar          pg_catalog       
          public  product               3  price            int4       4                 10      0  25                                           DEFAULT_CONSTRAINT   false        false                           false     2                                                    NO                  25                                           YES                  0                                                    0                     NO                          hotrod                product                               NEVER                                                                                                                     NO                                           0                                                                         price               0                                                                                     NO                                                                       0                                                                                                        3                                                 public               integer                      hotrod              32                        3                      0                                                     int4             pg_catalog       
          public  product               4  sku              int8       -5                19      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                product                               NEVER                                                                                                                     NO                                           0                                                                         sku                 0                                                                                     NO                                                                       0                                                                                                        4                                                 public               bigint                       hotrod              64                        4                      0                                                     int8             pg_catalog       
          public  transaction           1  account_id       int4       4                 10      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                transaction                           NEVER                                                                                                                     NO                                           0                                                                         account_id          0                                                                                     NO                                                                       0                                                                                                        1                                                 public               integer                      hotrod              32                        1                      0                                                     int4             pg_catalog       
          public  transaction           2  seq_id           serial     4                 10      0  nextval('transaction_seq_id_seq'::regclass)  IDENTITY_BY_DEFAULT  true         false                           false     2                                                    NO                  nextval('transaction_seq_id_seq'::regclass)  YES                  0                                                    0                     NO                          hotrod                transaction                           NEVER                                                                                                                     NO                                           0                                                                         seq_id              0                                                                                     NO                                                                       0                                                                                                        2                                                 public               integer                      hotrod              32                        2                      0                                                     int4             pg_catalog       
          public  transaction           3  time             varchar    12                16      0                                                                    false        false                           false     0                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                transaction                           NEVER                                                                                                                     NO                                           0                                                                         time                16                                                                                    NO                                                                       0                                                                                                        3                                                 public               character varying            hotrod              0                         3                      64                                                    varchar          pg_catalog       
          public  transaction           4  amount           int4       4                 10      0                                                                    false        false                           false     2                                                    NO                                                               YES                  0                                                    0                     NO                          hotrod                transaction                           NEVER                                                                                                                     NO                                           0                                                                         amount              0                                                                                     NO                                                                       0                                                                                                        4                                                 public               integer                      hotrod              32                        4                      0                                                     int4             pg_catalog       
          public  transaction           5  fed_branch_id    int8       -5                19      0                                                                    false        false                           true      2                                                    YES                                                              YES                  0                                                    0                     NO                          hotrod                transaction                           NEVER                                                                                                                     NO                                           0                                                                         fed_branch_id       0                                                                                     NO                                                                       0                                                                                                        5                                                 public               bigint                       hotrod              64                        5                      0                                                     int8             pg_catalog       
```

