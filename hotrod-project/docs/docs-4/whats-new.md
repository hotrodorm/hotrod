# What's New in HotRod 4

HotRod 4.0 includes several major features as well as a many of minor improvements.


## New Features in 4.0

### Main Functionality

- Schema discovery.
- Support for multiple datasources.
- Configuration property generator removed.
- Default values for the `<mappers>` tag. It can be now omitted in the configuration file.
- Reusing JDBC connection when using result-set processor.

### CRUD

- `propertiesChangeLog` is now removed from VOs to facilitate quick Spring prototyping.
- Nitro defaults to result-set generation now. The `<select-generation>` tag can be omitted by default.
- Classic FK Navigation is now enabled by default. The `<classic-fk-navigation>` tag can be omitted now.
- Nitro select methods in entity tags (`<table>` and `<view>` tags) are restricted to return the corresponding entity VOs only. They don't accept the 'vo' attribute anymore.

### LiveSQL

- LiveSQL now implements INSERT, UPDATE, and DELETE statements in addition to SELECT.
- LiveSQL supports the wildcard `*` symbol with filtering and aliasing.
- LiveSQL can use the DUAL (Oracle) and SYSIBM.SYSDUMMY1 (DB2 and Derby) system tables.
- LiveSQL now returns `List<Row>` and `Cursor<Row>`.



