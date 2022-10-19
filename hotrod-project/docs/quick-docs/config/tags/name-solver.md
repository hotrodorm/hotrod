# The `<name-solver>` Tag

<<<<<<< HEAD
HotRod's configuration can include a rule based *Name Solver* that can produce tailored names for database tables, views, 
and columns according to rules defined by the developer. 
=======
This tag controls adds rules to tailor the generation of classes and properties in the persistence layer.
>>>>>>> f3b425bb943f793247308dc8d84df8c5d38140c4

The `<name-solver>` tag has not attributes and can include one or more `<name>` tags.

It can serve as a centralized point for naming rules, for applications that have a somewhat standard strategy to name
database objects, and where the developer wants to modify or simplify these names when seen by the application.

In order to add a Name Solver the developer can add the `<name-solver>` tag as shown below:

```xml
<name-solver>
  <name value="^sk(.+)$" replace="prop2$1" scope="column" />
  <name value="^CLI_(.+)$" replace="$1" scope="column" />
  <name value="^(.+)_TBL$" replace="$1" scope="table, view " />
  <name value="^(.+)_GEN$" replace="$1" />
  <name value="^\w{3,3}_(\w+)_CLI$" replace="$1" scope="table, column" />
  <name value="^CLI_(\w.+)_(\w+)_\w{2,3}$" replace="$1$2" scope="table , column , view" />
</name-solver>
```

## Location

See [Configuration File Structure](../configuration-file-structure.md) for the correct location of this tag in the configuration file.

## Precedence

When a table, view, or column is inpected its persistence layer name is produced according to the following precedence:

1. If a `java-name` attribute is specified in the corresponding `<table>`, `<enum>`, `<view>`, or `<column>` tag this name
is selected and the Name Solver is not used.

2. Otherwise, if a rule in the Name Solver matches the database object, the object name is replaced according to this rule. Rules are evaluated in the order they are defined; once one rule matches the name is considered resolved and the rest of the rules are ignored.

3. Finally, the original or replaced name is used by HotRod to generate a camel case Java identifier for the database object.

## Rules

Rules are evaluated in order. If a rule matches, it's selected and no further rules are processed.

- Each rule includes a `value` attribute that represents a regex pattern to match against the database object name.

- Each rule includes a `scope` attribute that indicates if the rule applies to tables, view, and/or columns. It includes a comma-separated list of these options. If the scope is not specified then the rule applies to all scopes.

- If the database name matches the pattern in the `value` attribute and the rule is of the appropriate scope, the rule is selected to produce a new *replaced name* for the database object (table, view, or column).

- The replaced name is computed using the `replace` attribute. This pattern can use up to 9 pattern captures (from `$1` to `$9`) captured by the `value` pattern. For an explanation on captures see [Pattern Groups and Capturing](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#cg).

- The replaced name is considered a *database name* in the same category as the original object name. This means the replaced name takes the place of the original name, and is
converted to the target language (such as Java) according to the language rules. For example, if:

- The original column name `ACC_VIP_CLIENT_NBR`,
- Was converted to `VIP_CLIENT` by stripping the prefix and suffix,
- HotRod will convert it to camel case as the Java property `vipClient`.

## Examples

The following cases show combinations of regex `value` and `replace` patterns applied to database column names: 

| `value`  | `replace` | Database Object Name | Replaced Column Name | Java Property |
|--------|---------|-------------|----------------------|---------------|
| `^CLI_(\w.+)_(\w+)_\w{2,3}$` | `$1$2` | CLI_first_name_ATT | firstname | firstname |
| `^CLI_(\w.+)_(\w+)_\w{2,3}$` | `$1_$2` | CLI_first_name_ATT | first_name | firstName |
| `^CLI_(\w.+)_(\w+)_\w{2,3}$` | `$2` | CLI_first_name_ATT | name | name |
| `^CLI_(\w.+)_(\w+)_\w{2,3}$` | `$1_$2` | CLI_first_name_A | first_name_A | firstNameA |

