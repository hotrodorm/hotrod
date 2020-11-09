# The Name Solver

HotRod's configuration can include a rule based *Name Solver* that can produce modified names for database tables, views, 
and columns according to rules defined by the developer.

It can serve as a centralized point for naming rules, for applications that have a somewhat standard strategy to name
database objects, but the developer wants to simplify these names as seen by the application.

In order to add a Name Solver the developer can add the `<name-solver>` tag as shown below:

    <name-solver>
      <name value="^sk(.+)$" replace="prop2$1" scope="column" />
      <name value="^CLI_(.+)$" replace="$1" scope="column" />
      <name value="^(.+)_TBL$" replace="$1" scope="table, view " />
      <name value="^(.+)_GEN$" replace="$1" />
      <name value="^\w{3,3}_(\w+)_CLI$" replace="$1" scope="table, column" />
      <name value="^CLI_(\w.+)_(\w+)_\w{2,3}$" replace="$1$2" scope="table , column , view" />
    </name-solver>

## Location

See [Configuration File Structure](configuration-file-structure.md) for the correct location of this tag in the configuration file.

## Precedence

When HotRod encounters a table, view, or column its name is produced according to the following precedence:

1. If a `java-name` attribute is specified in the corresponding `<table>`, `<enum>`, `<view>`, or `<column>` tag this name
is selected and the Name Solver is not used.

2. Otherwise, if a rule in the Name Solver matches the database object, the object name is replaced according to this rule.
**Note**: Rules are evaluated in the order they are defined; once one rule matches the name is considered resolved and the rest of the rules are ignored.

3. Finally, the original or replaced name is used by HotRod to generate a camel case Java identifier for the database object.

## Rules

Rules are evaluated in order. If a rule matches, it's selected and no further rules are processed.

- Each rule includes a `value` attribute that represents a regex pattern to match against the database object name.

- Each rule includes a `scope` attribute that indicates if the rule applies to tables, view, and/or columns. It includes a comma-separated list of these options. If the scope is not specified then the rule applies to all scopes.

- If the database name matches the pattern in the `value` attribute and the rule is of the appropriate scope, the rule is selected to produce a new *replaced name* for the database object (table, view, or column).

- The replaced name is computed using the `replace` attribute. This pattern can use up to 9 pattern captures (from `$1` to `$9`) captured by the `value` pattern. For an explanation on captures see [Pattern Groups and Capturing](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#cg).

- The replaced name is considered a *SQL name* in the same category as the original object name. This name is always further processed by HotRod to produce an camel case Java property name. For example, if:

    - The original column name `ACC_VIP_CLIENT_NBR`,
    - Was converted to `VIP_CLIENT` by stripping the prefix and suffix,
    - HotRod will convert it to camel case as the Java property `vipClient`.

## Examples

The following cases show combinations of regex `value` and `replace` patterns applied to database column names: 

<table style="border: 1px solid red; border-collapse: collapse; text-align: left;">
  <tr style="border: 1px solid red;">
    <th style="border: 1px solid red;padding: 5px;">value</th>
    <th style="border: 1px solid red;padding: 5px;">replace</th>
    <th style="border: 1px solid red;padding: 5px;">Column Name</th>
    <th style="border: 1px solid red;padding: 5px;">Replaced Column Name</th>
    <th style="border: 1px solid red;padding: 5px;">Java Property</th>
  </tr>
  <tr style="border: 1px solid red;">
    <td style="border: 1px solid red;padding: 5px;">^CLI_(\w.+)_(\w+)_\w{2,3}$</td>
    <td style="border: 1px solid red;padding: 5px;">$1$2</td>
    <td style="border: 1px solid red;padding: 5px;">CLI_first_name_ATT</td>
    <td style="border: 1px solid red;padding: 5px;">firstname</td>
    <td style="border: 1px solid red;padding: 5px;">firstname</td>
  </tr>
  <tr style="border: 1px solid red;">
    <td style="border: 1px solid red;padding: 5px;">^CLI_(\w.+)_(\w+)_\w{2,3}$</td>
    <td style="border: 1px solid red;padding: 5px;">$1_$2</td>
    <td style="border: 1px solid red;padding: 5px;">CLI_first_name_ATT</td>
    <td style="border: 1px solid red;padding: 5px;">first_name</td>
    <td style="border: 1px solid red;padding: 5px;">firstName</td>
  </tr>
  <tr>
    <td style="border: 1px solid red;padding: 5px;">^CLI_(\w.+)_(\w+)_\w{2,3}$</td>
    <td style="border: 1px solid red;padding: 5px;">$2</td>
    <td style="border: 1px solid red;padding: 5px;">CLI_first_name_ATT</td>
    <td style="border: 1px solid red;padding: 5px;">name</td>
    <td style="border: 1px solid red;padding: 5px;">name</td>
  </tr>
  <tr>
    <td style="border: 1px solid red;padding: 5px;">^CLI_(\w.+)_(\w+)_\w{2,3}$</td>
    <td style="border: 1px solid red;padding: 5px;">$1_$2</td>
    <td style="border: 1px solid red;padding: 5px;">CLI_first_name_A</td>
    <td style="border: 1px solid red;padding: 5px;">first_name_A</td>
    <td style="border: 1px solid red;padding: 5px;">firstNameA</td>
  </tr>
</table>


  
