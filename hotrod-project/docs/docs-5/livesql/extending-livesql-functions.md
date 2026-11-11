# Custom LiveSQL Functions

LiveSQL includes the infrastructure to add custom database functions. These functions can represent built-in database functions, or other user-defined functions available in the database.

## Example

We want to create the new function `random` that produces a random numeric value in the query between zero (included) and a numeric parameter N (excluded).

First, we define the SQL template in the configuration file. We include the following section in the XML file:

```
<livesql>
  <function name="random" template="(random() * (#{}))" />
</livesql>
```

Note: since the random() function in PostgreSQL produces numbers between 0 and 1 only, this template adds the extra functionality to multiply it by the parameter N we want to supply. If we just wanted a value between 0 and 1 the template would be simply `random()`.

Second, to add the new function create a `@Component` that include the method for the function. For example:

```java
@Component
public class PostgreSQL12Functions {

  public NumericFunction random(NumericExpression n) {
    return Function.returnsNumeric(LiveSQLFunctions.random, n);
  }

  public NumericFunction random(Number n) {
    return Function.returnsNumeric(LiveSQLFunctions.random, BoxUtil.box(n));
  }

}
```

The property `LiveSQLFunctions.random` represents the SQL template used in the SQL query, is defined in the configuration file, and is generated/updates during the persistence layer generation.

The function is defined twice to accept database expressions and/or columns, and also literal application numbers. Therefore, we can call this function using  `pg.random(t.queueSize.minus(t.delivered))` as well as with a number `pg.random(100)`.


Finally, add it to your class:

```java
@Autowired
private PostgreSQL12Functions pg;
```

And use it:

```java
sql.select(pg.random(100)).execute()
```

To produce a result such as: `79.6045811049445`

# Factory methods

LiveSQL groups all SQL parameters into six broad types: numbers, strings, date/time, boolean, binary/byte arrays, and generic objects.

Therefore, you can use any of the six factory methods available in the `Function` class to create a function with the corresponding LiveSQL return type:

- `Function.returnsNumeric(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `NumericFunction`
- `Function.returnsChar(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `CharFunction`
- `Function.returnsDateTime(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `DateTimeFunction`
- `Function.returnsBoolean(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `BooleanFunction`
- `Function.returnsBinary(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `BinaryFunction`
- `Function.returnsObject(LiveSQLFunctionTemplate template, Expression... parameters)` -- to produce a `ObjectFunction`

**Note**: The `CURSOR` data type is not supported, either as an IN/OUT parameter or as a return type. Also, the `INTERVAL` type is not implemented yet, but it may be supported in the future.

# Parameters

The abstract function classes must receive parameters that extend `org.hotrod.runtime.livesql.expressions.Expression`. Typically these are of the types:

 - `NumericExpression`
 - `CharExpression`
 - `DateTimeExpression`
 - `Predicate` (boolean expression)
 - `BinaryExpression`
 - `ObjectExpression`
 - `Expression` -- A generic type that accepts any of the above

Notice the function factory receives either a vararg (`Expression...`) or a *single* array (`Expression[]`). If your function receives multiple separate parameters that you need to assemble as a single array parameter, you can bundle together using `Function.bundle(a, b)`.

## Parameters Boxing

Primitive values and simple parameters like:

 - Any `java.lang.Number` such as `int`, `Integer`, or `Float`
 - `String`
 - `java.util.Date`
 - `boolean` or `Boolean`
 - `byte[]`
 - any `Object`

need to be promoted to `Expression` to be used in a LiveSQL query. You can do this:

 - while typing a LiveSQL query using `sql.val(Number|String|Date|Boolean|byte[]|Object)`, or
 - by offering multiple constructors in the custom function definition and then boxing simple parameters as `Expression` using `BoxUtil.box(x)`, as shown in the `sin()` function below:

```java
public NumericFunction sin(NumericExpression x) { // already an Expression parameter
  return Function.returnsNumeric("sin(#{})", x);
}

public NumericFunction sin(Number x) { // accepts any java.lang.Number
  return sin(BoxUtil.box(x));
}
```

In this way, the above function can be called with an Expression as in `sin(a.angle)`, or with a primitive value as in `sin(123)`.

# Function Templates

The function template specifies the SQL rendering details of the function. For example, `sin(#{})` renders as `sin(x)` where `x` is replaced by the given parameter when executing the query. A template can include two types of parameters:

 - *Single positional parameters* as `#{}`.
 - *Varargs parameters* as `#{prefix?suffix}` or `#{firstprefix?prefix?suffix}`.

Note that varargs can only be placed as the last parameter of the template; on a vararg parameter `prefix`, `suffix`, and `firstPrefix` can be omitted, as necessary.

## Template Examples

The following examples illustrate the templated to use if you want to add a few PostgreSQL built-in functions into the LiveSQL syntax:

 - `localtimestamp` -- function with no parenthesis.
 - `random()` -- no parameters.
 - `sin(#{})` -- one parameter.
 - `left(#{}, #{})` -- two parameters.
 - `format(#{}#{, ?})` -- a single parameter, plus varargs prepended with comma-space and no suffix.
 - `coalesce(#{?, ?})` -- vararg: first prefix is empty, main prefix is comma-space, and no suffix.
 - `substring(#{} from #{} for #{})` -- three parameters with custom template.
 - `(#{} || #{})` -- concatenation; parenthesis added to enforce correct precedence.
 - `cast(#{} as numeric)` -- typical cast using custom template.
 - `(#{})::numeric` -- alternative cast; parenthesis added to enforce correct precedence.
 
# User-Defined Functions

As well as built-in functions, the developer can also include user defined functions as part of LiveSQL.

For example, if the developer created the following function in a PostgreSQL database:

```sql
create function addone(in a integer) returns integer
as $bodytag$
begin
  return a + 1;
end;
$bodytag$ language plpgsql;
//
```

A class could be used to provide the function to LiveSQL. For example:

```java
@Component
public class MyFunctions {

  public NumericFunction addone(NumericExpression a) {
    // template: addone(#{})
    return Function.returnsNumeric(LiveSQLFunctions.addone, a);
  }

  public NumericFunction addone(Number a) {
    return addone(BoxUtil.box(a));
  }

}
```

and could be used in the LiveSQL query, as in:

```java
@Autowired
private MyFunctions mf;

...

sql.select(mf.addone(123)).execute()
```

To produce the result `124`.

## Examples

The examples below were tested in PostgrrSQL 12 but work in the same way in any other supported database. PostgreSQL implements functions with varied templates. Some use the traditional comma as separator while other ones use keywords; most have parentheses, some don't; there are also some functions that use the syntax as of operators.

- **Example #1:** Function `localtimestamp` with no parameters and no parenthesis.
 
  This example demonstrate that any template can be used, even without parenthesis.

    ```java
    public DateTimeFunction localtimestamp() {
      // template specified in the XML configuration: localtimestamp
      return Function.returnsDateTime(LiveSQLFunctions.localtimestamp);
    };
    ```

- **Example #2:** Function `random()` with no parameters.

  As well as in the previous example, this function does not have any parameters, but needs the parenthesis in its rendering.

    ```java
    public NumericFunction random() {
      // template specified in the XML configuration: random()
      return Function.returnsNumeric(LiveSQLFunctions.random);
    }
    ```

- **Example #3:** Function `sin(x)` with a single parameter.

  Two factory methods are implemented: the first one receives a boxed value (NumberExpression in this case),
  while the second method receives an unboxed `java.lang.Number` (such as `int`, `Integer`, `Double`, etc.).

  Notice the second method only deals with the parameter boxing. It boxes it into an Expression by
  using `BoxUtil.box(x)`. Once done it reuses the first method, to avoid defining the template again.

    ```java
    public NumericFunction sin(NumericExpression x) {
      // template specified in the XML configuration: sin(#{})
      return Function.returnsNumeric(LiveSQLFunctions.sin);
    }

    public NumericFunction sin(Number x) {
      return sin(BoxUtil.box(x));
    }
    ```

- **Example #4:** Function `coalesce(a, ...)` with a vararg.

  Six factory methods are included, one per Expression type. Notice the template is a vararg `#{?, ?}`. Also notice that it also
  bundles the received parameters using `Function.bundle(a, b)` since the factory can only receive one parameter of type `Expression[]`.

  Notice that the factory methods need to accept at least one parameter to clearly distinguish the type of the function:

    ```java
    public NumericFunction coalesce(NumericExpression a, NumericExpression... b) {
      // template specified in the XML configuration: coalesce(#{?, ?})
      return Function.returnsNumeric(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }

    public CharFunction coalesce(CharExpression a, CharExpression... b) {
      return Function.returnsChar(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }

    public DateTimeFunction coalesce(DateTimeExpression a, DateTimeExpression... b) {
      return Function.returnsDateTime(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }

    public BooleanFunction coalesce(Predicate a, Predicate... b) {
      return Function.returnsBoolean(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }

    public BinaryFunction coalesce(BinaryExpression a, BinaryExpression... b) {
      return Function.returnsBinary(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }

    public ObjectFunction coalesce(ObjectExpression a, ObjectExpression... b) {
      return Function.returnsObject(LiveSQLFunctions.coalesce, Function.bundle(a, b));
    }
    ```

- **Example #5:** Function `left(text, n)` with two parameters.

  It includes variations for both parameters being boxed or unboxed to produce four similar methods:

  Again notice that, to avoid redundancy, the template is defined in the first variation only. The second, third, and fourth
  methods box the parameters and then reuse the first method.

    ```java
    public CharFunction left(CharExpression text, NumericExpression n) {
      // template specified in the XML configuration: left(#{}, #{})
      return Function.returnsNumeric(LiveSQLFunctions.left, text, n);
    }

    public CharFunction left(String text, NumericExpression n) {
      return left(BoxUtil.box(text), n);
    }

    public CharFunction left(CharExpression text, Number n) {
      return left(text, BoxUtil.box(n));
    }

    public CharFunction left(String text, Number n) {
      return left(BoxUtil.box(text), BoxUtil.box(n));
    };
    ```

- **Example #6:** Function `format(text, args...)` with one positional parameter and a vararg.

  It includes boxed and unboxed variations for the first parameter only. It
  assumes the second will always be boxed:

    ```java
    public CharFunction format(CharExpression text, Expression... args) {
      // template specified in the XML configuration: format(#{}#{, ?})
      return Function.returnsNumeric(LiveSQLFunctions.format, Function.bundle(text, args));
    }

    public CharFunction format(final String text, final Expression... args) {
      return format(BoxUtil.box(text), args);
    }
    ```

- **Example #7:** Function `set_byte(string, offset, newvalue)` with three positional parameters.

  The example below considers only the variation where all parameters are unboxed. 
  All other seven variations should probably be implemented as in the "left" function above, in order to accept any combination of boxed and unboxed parameters:

    ```java
    public BinaryFunction set_byte(byte[] string, Number offset, Number newValue) {
      // template specified in the XML configuration: set_byte(#{}, #{}, #{})
      return Function.returnsNumeric(LiveSQLFunctions.setByte,
          Function.bundle(Function.bundle(BoxUtil.box(string), BoxUtil.box(offset)), BoxUtil.box(newValue)));
    }
    ```

