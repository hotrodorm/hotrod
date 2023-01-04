# JDBC Catalogs and Schemas

Typically databases support a schema, a catalog, or both. The table shown below is a quick guide for 
the support they have on each of database:

| Database          | Supports Catalog  | Supports Schema |  
| ----------------  | :--------------:  | :-------------: | 
| Oracle            | --                | Yes             | 
| DB2               | --                | Yes             | 
| PostgreSQL        | --                | Yes             | 
| SQL Server        | Yes               | Yes             | 
| MariaDB           | Yes               | --              | 
| MySQL             | Yes               | --              | 
| SAP ASE (ex-Sybase)  | Yes               | Yes             | 
| H2                | Yes/No[^1]                | Yes             | 
| HyperSQL          | --                | Yes             | 
| Apache Derby      | --                | Yes             | 

[^1]: H2 sometimes requires the catalog name when searching for tables. When required, the catalog
corresponds to the database name.