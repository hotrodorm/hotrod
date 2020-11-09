# Configuration File Structure

HotRod generates code according to the metadata retrieved from a live existing database, typically 
in the DEV environment.

Apart from the database metadata some extra details are needed to fully produce the code. These fall into:

- Extra database details, such as sequences to use for each table, extra schemas to inspect, metadata inspection strategies, tables to treat as enums, etc.
- Generation details: base directories, package structure, class naming rules, data type rules, foreign key generation, optimistic locking details, etc.
- Nitro Queries: general purpose, flat, and structured queries.
- Configuration Facets & Fragments.

## The Configuration File

Although it can have any name, typically the main configuration file is called `hotrod.xml`. It takes the form:

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
    
      <table>
      <view>
      <enum>
      <dao>
      <converter>
      <facet>
      <fragment>
    
    </hotrod-fragment>

## See Also

- [<name-solver>](name-solver.md) tag
- [<type-solver>](type-solver.md) tag
- [Nitro Queries](../nitro/nitro-queries.md)
 
 
 