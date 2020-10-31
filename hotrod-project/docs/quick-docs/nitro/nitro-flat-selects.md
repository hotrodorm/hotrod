## Nitro Flat SELECTs

A Nitro Flat (or unstructured) SELECT is the simplest and probably most useful and common form of the &lt;select> tag. It produces a single value 
object class that represents a row of the result set. The execution returns a `java.util.List` of these value objects. For example:

    <dao name="WebQueriesDAO">
    
      <select method="findActiveAccountsWithClient" vo="AccountClientVO">
        select a.*, c.name, c.type as "client_type"
        from account a
        join client c on c.id = a.client_id
        where a.active = 1
      </select>
      
    </dao>

This select specifies that:

 - A new `WebQueriesDAO` class will be generated.
 - This DAO will include the method `findActiveAccountsWithClient` to execute this query.
 - The method will return a `List&lt;AccountClientVO>`.
 - The abstract value object `AbstractAccountClientVO` will be generated. It will include all columns of the result set as fully 
 typed Java, fully named Java properties. 
 - A concrete value object will be also generated, so the developer can add custom properties and behavior. **Important**: this concrete value object is never updated when/if the HotRod generation is executed again and again; in other words, any custom change written by the developer is safe here.

As an example, the &lt;select> tag above could produce the value objects:

    public class AbstractAccountClientVO {
      protected Integer id = null;
      protected Long amount = null;
      protected Integer clientId = null;
      protected String name = null;
      protected Short clientType = null;
      // getters and setters omitted
    }
    
    @Component
    public class AccountClientVO extends AbstractAccountClientVO {
      // Add custom properties and behavior here
    } 

Additionally, the generated DAO class that will expose the specified method could look like:

    @Component
    public class WebQueriesDAO implements Serializable, ApplicationContextAware {
    
      public List<AccountClientVO> findActiveAccountsWithClient() {
        ...
      }
      
    }

## Query Return Mode

Flat Selects can work with three return modes: list, cursor, and single-row.

### The `list` mode

The `list` mode is the default mode and returns a `java.util.List<VO>`. 

This mode reads all the rows returned by the query into memory, assembles a list, and returns this list
to the caller.

**Note**: This strategy can become a memory hog if the query returns a massive number of rows; in cases like this the `cursor` mode can be of use.

Example:

    <select method="findOldAccounts" vo="OldAccountVO">
      ...
    </select>

Equivalent to:

    <select method="findOldAccounts" vo="OldAccountVO" mode="list">
      ...
    </select>

### The `cursor` mode

The `cursor` mode returns a `org.hotrod.runtime.cursors.Cursor<VO>`.

The `Cursor` object is a forward-only iterable object that uses a buffer 
to receive a limited number of rows at a time from the query, thus limiting the amount of allocated memory at any given time.
Cursors can be a memory-efficient solution for processes that handle massive number of rows that need to be
**read, processed, and discarded immediately**.

**Note**: The memory efficiency benefit of a cursor is not leveraged if the process ends up assembling all the rows
into a `List` (or other `Collection`-like) data structure that will hold all the rows in memory anyway.

Example:

    <select method="findOldAccounts" vo="OldAccountVO" mode="cursor">
      ...
    </select>

### The `single-row` mode

The `single-row` mode returns a single `VO`. 

It assumes the query returns zero or one row at most. 

**Note**: It's the responsibility of the developer to ensure the query returns at most one row. If at runtime the query returns more than a single row, the query is considered failed and an error is displayed, such as:

> Expected one result (or null) to be returned by SELECT, but found: 3

Example:

    <select method="findSingleAccount" vo="SingleAccountVO" mode="single-row">
      ...
    </select>
    
## Parameters and the &lt;complement> tag

Most likely your query will need parameters for its execution. You can add parameters using the &lt;parameter> tag inside the &lt;select> tag, as in:

    <parameter name="accountId" java-type="java.lang.Integer" jdbc-type="NUMERIC" />

This tag specifies:

 - There will be a Java parameter called `accountId` in the method.
 - This parameter will be of type `Integer`.
 - In case this parameter is null its JDBC type will be informed to the database as `NUMERIC`. See the JDBC Specification for details on this requirement.
 
The &lt;select> tag can include multiple &lt;parameter> tags.

If this &lt;parameter> tag were included in the example above, the signature of the `findActiveAccountsWithClient` could change to:

    public List<AccountClientVO> findActiveAccountsWithClient(Integer accountId) {
      ...
    }

Now, parameters need be applied to the SQL statement at specific locations. The parameter takes the form `#{parameter-name}`; in this case, if its name is `accountId` the parameter should be applied as `#{accountId}`. 

Please consider that any parameter can be applied multiple times in the SQL statement; there's no limitation for this same parameter to show up multiple times in the same query.

Additionally, when adding a parameter, we need to make sure the resulting SQL statement will be valid when evaluated by HotRod. Since a parameter like `#{accountId}` won't be valid in SQL, we'll need to inform HotRod to exclude part of the SQL statement using the &lt;complement> tag. For example, if the query above had this parameter we could apply it as:

      <select method="findActiveAccountsWithClient" vo="AccountClientVO">
      
        <parameter name="accountId" java-type="java.lang.Integer" jdbc-type="NUMERIC" />
        
        select a.*, c.name, c.type as "client_type"
        from account a
        join client c on c.id = a.client_id
        where a.active = 1
          <complement>
          and a.id = #{accountId}
          </complement>
        
      </select>

Excluding the section `and a.id = #{accountId}` makes the SELECT a fully valid SQL statement.

Also, we can add multiple &lt;complement> tags to exclude multiple sections that include parameters; any section inside these tags will be excluded by HotRod when evaluating the query.


 
























 

  

