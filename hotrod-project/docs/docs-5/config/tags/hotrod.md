# The `<hotrod>` Tag

This tag represents the entry point of the main HotRod configuration file. 

It first includes:

- A `<generators>` tag to configure the generator(s).
- An optional `<name-solver>` tag to define custom name processing.
- An optional `<type-solver>` tag to define rules for data types.

It then includes an unordered list of:

- `<table>` tag to include tables in the code generation.
- `<view>` tag to include views in the code generation.
- `<enum>` tag to include a enum-type tables in the code generation.
- `<dao>` tag to define DAOs that will group related queries.
- `<exclude>` tag to exclude a table or view from the auto-discovery of tables and views. See [auto-discovery])(./mybatis-spring.md).
- `<converter>` tag to define converters for complex data types. These converters can be used in any table, view, or DAOs.
- `<facet>` tag to to enable partial code generation.
- `<fragment>` tag to divide the configuration file into multiple files.

It can include zero or more of each of these tags in any order, even intermixed.

