# What's New in 4.0

HotRod 4.0 includes several major features as well as a myriad of minot improvements.

## New Features

- Schema discovery.
- Support for multiple datasources.
- LiveSQL now returns `List<Row>` and `Cursor<Row>` instead of `List<Map<String, Object>>` and `Cursor<Map<String, Object>>`.
- Nitro defaults to result-set generation now. The `<select-generation>` tag can be omitted by default.
- Classic FK Navigation is now enabled by default. The `<classic-fk-navigation>` tag can be omitted now.

## Other Improvements

- `propertiesChangeLog` is now removed from VOs to facilitate quick Spring prototyping.
- Configuration property generator removed.
- Default values for the `<mappers>` tag. It can be now omitted in the configuration file.
- Reusing JDBC connection when using result-set processor.

