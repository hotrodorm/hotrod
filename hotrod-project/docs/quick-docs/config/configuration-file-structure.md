# Configuration File Reference

The Configuration File of HotRod defines the details on how to generate the persistence code. It offers default for
the configuration parameters to offer a simple usage, but they can also be tweaked to tailor the code generation to
specific needs.

The configuration includes:

- The desired folder structure of the persistence code.
- The desired naming convention of the generated DAOs and VOs.
- Custom post-processing of tables and column names (`<name-solver>`).
- Custom rules to decide on data types for columns (`<type-solver>`).
- Converters to read and write complex types (`<converter>`).
- List of tables (and enums), views, and sequences to inspect in default and non-default schemas.
- Custom DAOs to gather related sets of queries.
- Combinable facets to enable quick, partial code refresh.

The configuration file can be broken into separate *fragments* to facilitate parallel development and to
segregate DAOs per groups (modules, development teams, etc.). Each fragment produces a separate Java package.

## Database Metadata 

HotRod generates the persistence code by inspecting the metadata retrieved from a live existing database (a *sandbox* database) that 
typically resides in the development environment.

Although database engines provide a copious amount of metadata for tables and other database objects, some
external metadata is still needed for the generation, as well as some extra configuration to tailor the
resulting persistence to each specific application. These extra details fall into the following categories:

- **Extra Metadata**: metadata inspection strategies, enums, sequences to use with each table, extra schemas to inspect, etc.
- **Generation Details**: base directories, package structure, object naming strategies, data type rules, foreign key generation, optimistic locking details, custom columns, data converters, etc.
- **Nitro Queries**: flat queries, general purpose queries, structured queries.
- **Project Structure**: Configuration fragments, generation facets.

## The Configuration File

The configuration file takes the general form:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod SYSTEM "hotrod.dtd">

<hotrod>

  <generators />
  <name-solver />
  <type-solver />
  
  <table />
  <view />
  <enum />
  <dao />
  <converter />
  <facet />
  <fragment />
  
</hotrod>
```

The general structure follow the rules below:

- The persistence-wide tags can only be included in the main configuration file. These are: `<generators>`, `<name-solver>` (optional), and `<type-solver>` (optional).

- Then, there can be zero or more `<table>`, `<view>`, `<enum>`, `<dao>`, `<converter>`, `<facet>`, and/or `<fragment>` tags; 
there can be any number of each one and they can be included in any order, even intermixed.

- `<facet>` tags must be named and can include zero or more `<table>`, `<view>`, `<enum>`, `<dao>` tags. All included tags will be considered part of the facet.

- `<fragment>` tags must reference a fragment file where extra configuration is specified. Fragments can also include other fragments, but any given 
fragment can only be included once in the configuration tree.


## The Fragment File Structure

A configuration fragment is a separate file that includes sections of the configuration, and in turn can include more fragments.

A fragment can include zero or more `<table>`, `<view>`, `<enum>`, `<dao>`, `<converter>`, `<facet>` and `<fragment>` tags. The main purpose of 
fragments is to separate the configuration into multiple files in order to reduce source code collisions when multiple developers are 
modifying the configuration simultaneously. This most commonly happens to [Nitro Queries](../nitro/nitro-queries.md) that need to be tweaked, expanded, 
and/or added frequently.

It's a common practice to separate fragments by application module and/or by the UI. Queries that are expected to suffer constant changes or improvements should be separated from more stable ones, or from the main configuration.

A fragment configuration file takes the general form:

```xml
<?xml version="1.0"?>
<!DOCTYPE hotrod-fragment>

<hotrod-fragment>

  <table />
  <view />
  <enum />
  <dao />
  <converter />
  <facet />
  <fragment />

</hotrod-fragment>
```

A fragment can include any number of each one of these tags, and they can be placed in any ordering, even intermixed.


## Complete Structure of the Configuration File

The complete structure of HotRod's main configuration file is shown below. 

You can look into each tag to get details of it:

* [`<hotrod>`](tags/hotrod.md)
    * [`<generators>`](tags/generators.md)
        * [`<mybatis-spring>`](tags/mybatis-spring.md)
            * [`<daos>`](tags/daos.md)
            * [`<mappers>`](tags/mappers.md)
            * [`<select-generation>`](tags/select-generation.md)
            * [`<classic-fk-navigation>`](tags/classic-fk-navigation.md)
            * [`<property>`](tags/property.md)
        * `<mybatis>` *obsolete*
        * `<spring-jdbc>` *obsolete*
    * [`<name-solver>`](tags/name-solver.md)
        * `<name>`
    * [`<type-solver>`](tags/type-solver.md)
        * `<when>`
    * [`<table>`](tags/table.md)
        * [`<column>`](tags/column.md)
        * [`<auto-generated-column>`](tags/auto-generated-column.md)
        * [`<version-control-column>`](tags/version-control-column.md)
        * [`<sequence>`](tags/sequence.md)
        * [`<query>`](tags/query.md)
            * [`<parameter>`](tags/parameter.md)
            * *Dynamic SQL*: Any number of flat or nested [`<if>`](id.md), [`<choose>`](choose.md), [`<where>`](where.md), [`<set>`](set.md), [`<trim>`](trim.md), [`<foreach>`](foreach.md), [`<bind>`](bind.md)
        * [`<select>`](select.md)
            * `<parameter>`
            * [`<column>`](column.md)
            * [`<columns>`](columns.md)
                * [`<vo>`](vo.md)
                   * [`<association>`](association.md)
                     * `<association>`
                     * `<collection>`
                     * `<expression>`
                   * [`<collection>`](collection.md)
                        * `<association>`
                        * `<collection>`
                        * `<expression>`
                   * [`<expression>`](expression.md)
                * `<association>`
                * `<expression>`
            * *Dynamic SQL*: Any number of flat or nested `<if>`, `<choose>`, `<where>`, `<set>`, `<trim>`, `<foreach>`, `<bind>`
            * [`<complement>`](complement.md)
                * *Dynamic SQL*: Any number of flat or nested `<if>`, `<choose>`, `<where>`, `<set>`, `<trim>`, `<foreach>`, `<bind>`
    * [`<enum>`](enum.md)
        * [`<non-persistent>`](non-persistent.md)
    * [`<view>`](view.md)
        * `<column>`
        * `<sequence>`
        * `<query>`
        * `<select>`
    * [`<dao>`](dao.md)
        * `<sequence>`
        * `<query>`
        * `<select>`
    * [`<fragment>`](fragment.md)
    * [`<facet>`](facet.md)
        * `<table>`
        * `<enum>`
        * `<view>`
        * `<dao>`
    * [`<converter>`](./tags/converter.md)

The structure of a HotRod's fragment configuration file is:

 * [`<hotrod-fragment>`](hotrod-fragment.md)
    * `<table>`
    * `<enum>`
    * `<view>`
    * `<dao>`
    * `<fragment>`
    * `<facet>`
    * `<converter>`


## See Also

- [`<name-solver>`](name-solver.md) tag
- [`<type-solver>`](type-solver.md) tag
- [Nitro Queries](../nitro/nitro-queries.md)
 
 
 