# General queries using &lt;query>

The `<query>` tag allows the developer to include a SQL statement that does not return a result set.

Typically these correspond to UPDATE, DELETE statements, but can actually include any SQL statement such as INSERT, CREATE, ALTER, etc.

Each &lt;query> tag must included in a &lt;table>, &lt;view>, or &lt;dao> tag. The corresponding DAO class exposes the &lt;query> as a Java method.

Parameters can be also be added by using the &lt;parameter> tag, paired with the `#{x}` parameter insertions points to enhance the query. General queries do no require the use of the &lt;complement> tag to enclose parameters.

General queries can also be enhanced by the use of Dynamic SQL.
 