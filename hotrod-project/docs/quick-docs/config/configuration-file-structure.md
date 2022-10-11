# Configuration File Structure

HotRod generates code according to the metadata retrieved from a live existing database, typically 
in the DEV environment.

Apart from the database metadata some extra details are needed to fully produce the persistence logic. These details fall into different categories:

- Extra metadata such as sequences to use with each table, extra schemas to inspect, metadata inspection strategies, enums, etc.
- Generation details: base directories, package structure, class naming rules, data type rules, foreign key generation, optimistic locking details, etc.
- Nitro Queries: flat queries, general purpose queries, and structured queries.
- Configuration fragments & generation facets.

## The Configuration File

The configuration file takes the form:

    <?xml version="1.0"?>
    <!DOCTYPE hotrod SYSTEM "hotrod.dtd">
    
    <hotrod>
    
      <generators>
      <name-solver>
      <type-solver>
      
      <table>
      <view>
      <enum>
      <dao>
      <converter>
      
      <facet>
      
      <fragment>
      
    </hotrod>

The general structure follow the rules below:

- The tags `<generators>`, `<name-solver>` (optional), `<type-solver>` (optional) can only be included in the main configuration file, in the order shown above.

- Then, there can be zero or more of the `<table>`, `<view>`, `<enum>`, `<dao>`, `<converter>`, `<facet>`, and `<fragment>` tags; 
they can be intermixed and included in any order between them.

- A `<facet>` tag must be named and can include zero or more `<table>`, `<view>`, `<enum>`, `<dao>` tags, that belong to the facet.

- A `<fragment>` tag must reference the file where extra configuration is included.

## The Fragment File Structure

A configuration fragment is a separate file that include sections of the configuration, and in turn can include more fragments.

A fragment can include zero or more `<table>`, `<view>`, `<enum>`, `<dao>`, `<converter>`, `<facet>` and `<fragment>` tags. The main purpose of 
fragments is to separate the configuration into multiple files in order to reduce source code collisions when multiple developers are 
modifying the configuration simultaneously. This usually happens when using [Nitro Queries](../nitro/nitro-queries.md) that need to be tweaked, expanded, 
and/or added frequently.

A fragment configuration file takes the form:

    <?xml version="1.0"?>
    <!DOCTYPE hotrod-fragment>
    
    <hotrod-fragment>
    
      [config](./config.md)
    
      <table2> [<config>](./config.md)
      <view> [config](./config.md)
      <enum>
      <dao>
      <converter>
      <facet>
      <fragment>
    
    </hotrod-fragment>

## General Structure

The comprehensive structure of HotRod's main configuration file is:

 * [<hotrod>](hotrod.md)
    * [<generators>](generators.md)
        * [<mybatis-spring>](mybatis-spring.md)
            * [<daos>](daos.md)
            * [<mappers>](mapper.md)
            * [<select-generation>](select-generation.md)
            * [<classic-fk-navigation>](classic-fk-navigation-mybatis-spring.md)
            * [<property>](property.md)
            * *&lt;mybatis-configuration-template>* (obsolete)
        * *&lt;mybatis>* (obsolete)
        * *&lt;spring-jdbc>* (obsolete)
    * [<name-solver>](name-solver.md)
        * [<name>](name.md)
    * [<type-solver>](type-solver.md)
        * [<when>](when-type-solver.md)
    * [<table>](table.md)
        * [<column>](column.md)
        * [<select>](select.md)
            * [<parameter>](parameter.md)
            * [<column>](column.md)
            * [<columns>](columns.md)
                * [<vo>](vo.md)
                   * [<association>](association.md)
                     * &lt;association>
                     * [<collection>](collection.md)
                     * [<expression>](expression.md)
                   * &lt;collection>
                        * &lt;association>
                        * &lt;collection>
                        * &lt;expression>
                   * &lt;expression>
                * &lt;association>
                * &lt;expression>
            * [<complement>](complement.md)
                * [<if>](id.md)
                   * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                * [<choose>](choose.md)
                    * [<when>](when-choose.md)
                      * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                    * [<otherwise>](otherwise.md)
                      * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                * [<where>](where.md)
                      * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                * [<set>](set.md)
                      * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                * [<trim>](trim.md)
                      * Any number of &lt;if>, &lt;choose>, &lt;where>, &lt;set>, &lt;trim>, &lt;foreach>, &lt;bind>
                * [<foreach>](foreach.md)
                * [<bind>](bind.md)
        * [<query>](query.md)
            * &lt;parameter>
            * &lt;if>
            * &lt;choose>
            * &lt;where>
            * &lt;set>
            * &lt;trim>
            * &lt;foreach>
            * &lt;bind>
        * [<sequence>](sequence.md)
        * [<classic-fk-navigation>](classic-fk-navigation-table.md)
        * [<auto-generated-column>](auto-generated-column.md)
        * [<version-control-column>](version-control-column.md)
    * [<enum>](enum.md)
        * [<non-persistent>](non-persistent.md)
    * [<view>](view.md)
        * &lt;column>
        * &lt;select>
        * &lt;query>
        * &lt;sequence>
    * [<dao>](dao.md)
        * &lt;select>
        * &lt;query>
        * &lt;sequence>
    * [<fragment>](fragment.md)
    * [<facet>](facet.md)
        * &lt;table>
        * &lt;enum>
        * &lt;view>
        * &lt;dao>
    * [<converter>](converter.md)

The structure of a HotRod's fragment configuration file is:

 * [<hotrod-fragment>](hotrod-fragment.md)
    * &lt;table>
    * &lt;enum>
    * &lt;view>
    * &lt;dao>
    * &lt;fragment>
    * &lt;facet>
    * &lt;converter>



## See Also

- [<name-solver>](name-solver.md) tag
- [<type-solver>](type-solver.md) tag
- [Nitro Queries](../nitro/nitro-queries.md)
 
 
 