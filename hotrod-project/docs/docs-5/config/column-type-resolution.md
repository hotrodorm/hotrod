# Column Type Resolution

SELECT queries retrieve data in the form of result sets, each having a list of columns. When the data is received by the application, each column can be read in different ways.

If the application specifies the desired data type the JDBC driver and the database engine will try their best to honor it; behind the scenes this may entail a data conversion or data casting.

Otherwise, the JDBC driver and the database engine will choose a default data type deemed appropriate for the column type of the result set.

## Rules

Unfortunately, these default data types are not always adequate for the application. For example, a
DECIMAL(10,2) can be read as a `BigDecimal`, a `Float`, a `Double` or even as in `Integer` or `String`. Some database their JDBC drivers may allow some or all of these automatic conversions and/or casting while reading the data of a column.

This is when the *Column Type Resolution* comes into play. I short, the column type of the columns of a SELECT query is resolved according to a predefined logic.

## 1. Static Type Resolution &ndash; Tables and Views

For the main database entities of the application the columns data types are resolved in static manner during the persistence layer generation. This is always done in the CRUD logic for all tables and views included in the persistence layer.

Tables and views use the database entity metadata to compute the rules.

The data type of the columns of all tables and views are computed during the generation of the persistence layer according to the rules defined below. This type is set in a *static* way and cannot be changed afterwards.

For the tables and views included in the persistence layer the following rules apply, in order:

| Priority | Rule | Description |
| :--: | -- | -- |
| 1 | STATIC_DESIGNATED | Applies when the data type for the column is designated in the configuration with a `<column name="<column-name>" java-type="<type>" />` inside the corresponding `<select>`  tag |
| 2 | STATIC_LAYER_RULE | Applies when a `<entity>` rule defined in the `<type-solver>` tag matches the column. Static type solver rules can be used to define default custom types for specific database types |
| 3 | STATIC_DIALECT_RULE | Each HotRod database dialect defines default rules for most data types. See the rules for each database |

For each column the resolution is computed by type, starting at priority #1 and moving forward. The first rule that matches wins, and the rest are ignored.

**Note**: Data types resolved in static way are set in stone in the persistence layer. If you later on run the application in a different version of the database, or even in a different database altogether these data types won't change.

**Note**: If you change any `<column java-type>` tag or the `<type-solver>` tag it's recommended you regenerate the persistence layer to compute the data types according to the updated rules.

## 2. Static Type Resolution &ndash; Nitro SELECTs

For Nitro SELECT queries the columns data types are resolved in static manner during the persistence layer generation.

Unlike tables and views, Nitro uses the result set metadata instead of the entity metadata.

The data type of the columns of all tables and views are computed during the generation of the persistence layer according to the rules defined below. This type is set in a *static* way and cannot be changed afterwards.

For the tables and views included in the persistence layer the following rules apply, in order:

| Priority | Rule | Description |
| :--: | -- | -- |
| 1 | STATIC_DESIGNATED | Applies when the data type for the column is designated in the configuration with a `<column name="<column-name>" java-type="<type>" />` inside the corresponding `<table>` or `<view>` configuration tag |
| 2 | STATIC_LAYER_RULE | Applies when a  `<result-set>` rule defined in the `<type-solver>` tag matches the column. Static type solver rules can be used to define default custom types for specific database types |
| 3 | STATIC_DIALECT_RULE | TBD |

For each column the resolution is computed by type, starting at priority #1 and moving forward. The first rule that matches wins, and the rest are ignored.


## 3. Runtime Type Resolution &ndash; LiveSQL

LiveSQL queries retrieve column data from entities and also using SQL expressions computed by the database.

Entity columns are always read using the static data type computed during the persistence layer generation.

SQL expressions are read according to the following rules:

| Priority | Rule | Description |
| :--: | -- | -- |
| 1 | RUNTIME_DESIGNATED | When the data type is designated for the column in the LiveSQL query using `.type(<class>)` |
| 2 | RUNTIME_LAYER_RULE | Applies when a  `<result-set>` rule defined in the `<type-solver>` tag matches the column. These rules can be used to define default custom types for specific database types |
| 3 | RUNTIME_DIALECT_RULE | Resolved at runtime by a Runtime Dialect Default Rule |
| 4 | RUNTIME_JDBC_DRIVER_DEFAULT | Using JDBC driver default |

For each column the resolution is computed by type, starting at priority #1 and moving forward. The first rule that matches wins, and the rest are ignored.

## 4. Examples





