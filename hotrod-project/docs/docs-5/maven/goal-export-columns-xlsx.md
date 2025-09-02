# Exporting Column Metadata To an XLSX File

The `export-columns-xlsx` generates an XLSX Column Metadata Report for tables and views in the
database schema. The properties shown in this report can be essential to write custom logic in 
the `<type-solver>` and `<name-solver>` tags.

This report provides the same information as the [Export Column Metadata to TXT](goal-export-columns-txt.md).

## Apache POI Dependency

To export to XLSX format this plugin needs the extraApache POI dependency:

```xml
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>4.1.2</version>
  </dependency>
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>4.1.2</version>
  </dependency>
```

In short, your Maven plugin declaration may look like:

```xml
      <plugin>
        <groupId>org.hotrodorm.hotrod</groupId>
        <artifactId>hotrod-maven-plugin</artifactId>
        <version>${hotrod.version}</version>
        <configuration>
          <configfile>src/main/database/oracle/hotrod.xml</configfile>
          <jdbcdriverclass>${jdbcdriverclass}</jdbcdriverclass>
          <jdbcurl>${jdbcurl}</jdbcurl>
          <jdbcusername>${jdbcusername}</jdbcusername>
          <jdbcpassword>${jdbcpassword}</jdbcpassword>
          <jdbcschema>USER1</jdbcschema>
          <facets></facets>
          <display></display>
          <xlsxexportfile>./oracle-columns.xlsx</xlsxexportfile>
        </configuration>
        <dependencies>
        
          <dependency>
            <groupId>${jdbcdrivergroupid}</groupId>
            <artifactId>${jdbcdriverartifactid}</artifactId>
            <version>${jdbcdriverversion}</version>
          </dependency>
          
          <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.1.2</version>
          </dependency>

          <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.1.2</version>
          </dependency>
              
        </dependencies>
      </plugin>
```

 