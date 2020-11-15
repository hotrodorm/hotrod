# The Select Generation

The &lt;select-generation> tag allows the developer to choose a SQL processor and to configure it.

The &lt;select-generation> tag is optional and can be included in the &lt;mybatis-spring> tag, as in:

    <mybatis-spring>
      ...
      <select-generation strategy="result-set" />
      
      ...
    </mybatis-spring>

HotRod implements two SQL Processors:

- The traditional `create-view` processor, and
- The new `result-set` processor

By default HotRod uses the traditional `create-view` processor, but the developer can select the new one by using this configuration tag.

When using the traditional `create-view` processor, the additional `temp-view-base-name` attribute can be used to specify the views' base name that the processor uses.

## The `create-view` Processor

The `create-view` Processor is the traditional processor implemented in HotRod and it analyzes SQL queries by creating a temporary views in 
the database. 

In order to create views this processor requires the connecting user to have CREATE privileges in the database schema.

This processor accepts parameters in the `WHERE` clause of the main query or any subquery. However, the location where the developer
can place parameters using this processor is somewhat limited compared to the new `rrsult-set` processor. It also requires all parameters to be enclosed in
&lt;complement> tags.

This processor is enabled by default. It can also be explicitly set using the &lt;select-generation> tag, as in:

    <select-generation strategy="create-view" />

## The `result-set` Processor

The `result-set` Processor is the new processor implemented by HotRod and it analyzes queries by preparing JDBC Result Sets. 

It does not create views in the database schema, and the connecting user only needs read-only access to the database schema.

Parameters can be placed anywhere in the SQL query and do not need to be enclosed in &lt;complement> tags.

To enable this processor include the &lt;select-generation> tag, as in:

    <select-generation strategy="result-set" />

## Side-by-side Query Processor Comparison

The following table compares both Query Processors:


                    create-view Processor          Aspect          result-set Processor      
    -------------------------------------   --------------------   ------------------------ 
              Needs CREATE VIEW privilege   Database Privileges    Read-only access          
                     Only in WHERE clause    Parameter Location    Anywhere JDBC allows      
             Needs 2 database connections   Database Connections   Uses a single connection  
                  Slow on some databases*        Performance       Fast                      
                 May leave dangling views        Drawbacks         None                      
    Needed for parameters and Dynamic SQL     <complement> tag     Only for Dynamic SQL      

*It has been observed that Oracle database seems to be particular slow to retrieve database metadata; opening two connections
makes it slower.

The create-view Processor is enabled by default while the result-set Processor needs to be enabled.

 
  
      