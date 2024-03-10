# Configuration File Reference

This is the Configuration File Reference of the [HotRod ORM](../README.md).


## Complete Structure of the Configuration File

The configuration tags are shown below:

* [`<hotrod>`](tags/hotrod.md)
    * [`<generators>`](tags/generators.md)
        * [`<mybatis-spring>`](tags/mybatis-spring.md)
            * [`<discover>`](tags/discover.md)
                * [`<current-schema>`](tags/current-schema.md)
                    * [`<exclude>`](tags/exclude.md)
                * [`<schema>`](tags/schema.md)
                    * `<exclude>` *(see above)*
            * [`<daos>`](tags/daos.md)
            * [`<mappers>`](tags/mappers.md)
            * [`<select-generation>`](tags/select-generation.md)
            * [`<property>`](tags/property.md)
        * `<mybatis>` *obsolete*
        * `<spring-jdbc>` *obsolete*
    * [`<name-solver>`](tags/name-solver.md)
        * `<name>`
    * [`<type-solver>`](tags/type-solver.md)
        * `<when>`
    * [`<converter>`](./tags/converter.md)
    * [`<table>`](tags/table.md)
        * [`<column>`](tags/column.md)
        * [`<version-control-column>`](tags/version-control-column.md)
        * [`<sequence>`](tags/sequence.md)
        * [`<query>`](tags/query.md)
            * [`<parameter>`](tags/parameter.md)
            * *SQL query*
            * [Dynamic SQL](../nitro/nitro-dynamic-sql.md): Any number of flat or nested `<if>`, `<choose>`, `<where>`, `<set>`, `<trim>`, `<foreach>`, `<bind>`
        * [`<select>`](tags/select.md)
            * `<parameter>`
            * *SQL query*
            * [`<column>`](tags/column.md)
            * [`<columns>`](tags/columns.md)
                * [`<vo>`](tags/vo.md)
                   * [`<association>`](tags/association.md)
                     * `<association>`
                     * `<collection>`
                     * `<expression>`
                   * [`<collection>`](tags/collection.md)
                        * `<association>`
                        * `<collection>`
                        * `<expression>`
                   * [`<expression>`](tags/expression.md)
                * `<association>`
                * `<expression>`
            * Dynamic SQL: Any number of flat or nested `<if>`, `<choose>`, `<where>`, `<set>`, `<trim>`, `<foreach>`, `<bind>`
            * [`<complement>`](tags/complement.md)
                * Dynamic SQL: Any number of flat or nested `<if>`, `<choose>`, `<where>`, `<set>`, `<trim>`, `<foreach>`, `<bind>`
    * [`<enum>`](tags/enum.md)
        * `<non-persistent>`
    * [`<view>`](tags/view.md)
        * `<column>`
        * `<sequence>`
        * `<query>`
        * `<select>`
    * [`<dao>`](tags/dao.md)
        * `<sequence>`
        * `<query>`
        * `<select>`
    * [`<fragment>`](tags/fragment.md)
    * [`<facet>`](tags/facet.md)
        * `<table>`
        * `<enum>`
        * `<view>`
        * `<dao>`


This configuration file defines the details on how to generate the persistence code. It offers default values
for the configuration parameters that fit typical cases. These values can be tweaked to tailor the persistence layer
to specific needs.

In short, the configuration includes:

- The desired folder structure of the persistence code.
- The desired naming convention of the generated DAOs and VOs.
- Custom post-processing of tables and column names (`<name-solver>`).
- Custom rules to decide on data types for columns (`<type-solver>`).
- Converters to read and write complex types (`<converter>`).
- List of tables (and enums), views, and sequences to inspect in default and non-default schemas.
- Custom DAOs to gather related sets of queries.
- Combinable facets to enable quick, partial code refresh.


## Fragments

The configuration file can be broken into separate *fragments* to facilitate parallel development and to
segregate DAOs per groups (modules, development teams, etc.). Each fragment produces a separate Java package.


## Database Metadata 

HotRod generates the persistence code by inspecting the metadata retrieved from a live existing database (a *sandbox* database) that 
typically resides in the development environment.

Although database engines provide a copious amount of metadata for tables and other database objects, some
external metadata is needed for the generation and need to be specified in this file. These extra details fall into the following categories:

- **Extra Metadata**: metadata inspection strategies, enums, sequences to use with each table, extra schemas to inspect, etc.
- **Generation Details**: base directories, package structure, object naming strategies, data type rules, foreign key generation, optimistic locking details, custom columns, data converters, etc.
- **Nitro Queries**: flat queries, general purpose queries, and graph queries.
- **Project Structure**: Configuration fragments, generation facets.
