# Extending LiveSQL Functions

LiveSQL includes the infrastructure to add custom database functions. These functions can represent built-in 
database functions, or other user-defined functions available in the database.

To add new functions, create a `@Component` that include methods for each function. For example:

```java
@Component
public class PostgreSQL12Functions {

  public NumberFunction random() {
    return Function.returnsNumber("random()");
  }
  
}
```

Then, add it to your class:

```java
@Autowired
private PostgreSQL12Functions pg;
```

And use it:

```java
sql.select(pg.random()).execute()
```

To produce a result such as: `0.796045811049445`    

# Factory methods

LiveSQL groups all SQL parameters into six broad types: numbers, strings, date/time, boolean, binary/byte arrays, and generic objects.

Therefore, you can use any of the six factory methods available in the `Function` class to create a function with the corresponding LiveSQL return type:

- `Function.returnsNumber(String pattern, Expression... parameters)` -- to produce a `NumberFunction`
- `Function.returnsString(String pattern, Expression... parameters)` -- to produce a `StringFunction`
- `Function.returnsDateTime(String pattern, Expression... parameters)` -- to produce a `DateTimeFunction`
- `Function.returnsBoolean(String pattern, Expression... parameters)` -- to produce a `BooleanFunction`
- `Function.returnsByteArray(String pattern, Expression... parameters)` -- to produce a `ByteArrayFunction`
- `Function.returnsObject(String pattern, Expression... parameters)` -- to produce a `ObjectFunction`

**Note**: The `CURSOR` data type is not supported, either as an IN/OUT parameter or as a return type. Also, the `INTERVAL` type is not implemented yet, but it may be supported in the future.

# Parameters

The abstract function classes must receive parameters that extend `org.hotrod.runtime.livesql.expressions.Expression`. Typically these are of the types:

 - `NumberExpression`
 - `StringExpression`
 - `DateTimeExpression`
 - `Predicate` (boolean expression)
 - `ByteArrayExpression`
 - `ObjectExpression`
 - `Expression` -- to accept any of the above

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
public NumberFunction sin(NumberExpression x) { // already an Expression parameter
  return Function.returnsNumber("sin(#{})", x);
}

public NumberFunction sin(Number x) { // accepts any java.lang.Number
  return sin(BoxUtil.box(x));
}
```

In this way, the above function can be called with an Expression as in `sin(a.angle)`, or with a primitive value as in `sin(123)`.

# Function Pattern

The function pattern specifies the rendering details of the function. For example `sin(#{})` renders as `sin(x)` where `x` is replaced by the given parameter when executing the query. A pattern can include two types of parameters:

 - *Single positional parameters* as `#{}`.
 - *Varargs parameters* as `#{prefix?suffix}` or `#{firstprefix?prefix?suffix}`.

Please note that varargs can only be placed as the last parameter of the pattern; on a vararg parameter `prefix`, `suffix`, and `firstPrefix` can be omitted, as necessary.

## Pattern Examples

The following examples illustrate the patterns to use if you wanted to add a few PostgreSQL built-in functions into the LiveSQL syntax:

 - `localtimestamp` -- function with no parenthesis.
 - `random()` -- no parameters.
 - `sin(#{})` -- one parameter.
 - `left(#{}, #{})` -- two parameters.
 - `format(#{}#{, ?})` -- a single parameter, plus varargs prepended with comma-space and no suffix.
 - `coalesce(#{?, ?})` -- vararg: first prefix is empty, main prefix is comma-space, and no suffix.
 - `substring(#{} from #{} for #{})` -- three parameters with custom template.
 - `(#{} || #{})` -- concatenation; parenthesis added to enforce correct precedence.
 - `cast(#{} as numeric)` -- typical cast using custom pattern.
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

  public NumberFunction addone(NumberExpression a) {
    return Function.returnsNumber("addone(#{})", a);
  }

  public NumberFunction addone(Number a) {
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

The examples below were tested in PostgrrSQL 12. PostgreSQL implements functions with varied patterns. Some use the traditional comma as separator 
while other ones use keywords; most have parenthesis, some don't; there are also some functions that use the syntax as of operators.

- **Example #1:** Function `localtimestamp` with no parameters and no parenthesis.
 
  This example demonstrate that any pattern can be used, even without parenthesis.

    ```java
    public DateTimeFunction localtimestamp() {
      return Function.returnsDateTime("localtimestamp");
    };
    ```

- **Example #2:** Function `random()` with no parameters.

  As well as in the previous example, this function does not have any parameters, but needs the parenthesis in its rendering.

    ```java
    public NumberFunction random() {
      return Function.returnsNumber("random()");
    }
    ```

- **Example #3:** Function `sin(x)` with a single parameter. 

  Two factory methods are implemented: the first one receives a boxed value (NumberExpression in this case), 
  while the second method receives an unboxed `java.lang.Number` (such as `int`, `Integer`, `Double`, etc.).
  
  Notice the second method only deals with the parameter boxing. It boxes it into an Expression by
  using `BoxUtil.box(x)`. Once done it reuses the first method, to avoid defining the pattern again.
 
    ```java
    public NumberFunction sin(NumberExpression x) {
      return Function.returnsNumber("sin(#{})", x);
    }
    
    public NumberFunction sin(Number x) {
      return sin(BoxUtil.box(x));
    }
    ```

- **Example #4:** Function `coalesce(a, ...)` with a vararg.

  Six factory methods are included, one per Expression type. Notice the pattern is a vararg `#{?, ?}`. Also notice that it also
  bundles the received parameters using `Function.bundle(a, b)` since the factory can only receive one parameter of type `Expression[]`.
  
  Notice that the factory methods need to accept at least one parameter to clearly distinguish the type of the function:
  
    ```java
    public NumberFunction coalesce(NumberExpression a, NumberExpression... b) {
      return Function.returnsNumber("coalesce(#{?, ?})", Function.bundle(a, b));
    }
  
    public StringFunction coalesce(StringExpression a, StringExpression... b) {
      return Function.returnsString("coalesce(#{?, ?})", Function.bundle(a, b));
    }
  
    public DateTimeFunction coalesce(DateTimeExpression a, DateTimeExpression... b) {
      return Function.returnsDateTime("coalesce(#{?, ?})", Function.bundle(a, b));
    }
  
    public BooleanFunction coalesce(Predicate a, Predicate... b) {
      return Function.returnsBoolean("coalesce(#{?, ?})", Function.bundle(a, b));
    }
  
    public ByteArrayFunction coalesce(ByteArrayExpression a, ByteArrayExpression... b) {
      return Function.returnsByteArray("coalesce(#{?, ?})", Function.bundle(a, b));
    }
  
    public ObjectFunction coalesce(ObjectExpression a, ObjectExpression... b) {
      return Function.returnsObject("coalesce(#{?, ?})", Function.bundle(a, b));
    }
    ```

- **Example #5:** Function `left(text, n)` with two parameters.

  It includes variations for both parameters being boxed or unboxed to produce four similar methods:

  Again notice that, to avoid redundancy, the pattern is defined in the first variation only. The second, third, and fourth
  methods box the parameters and then reuse the first method.

    ```java
    public StringFunction left(StringExpression text, NumberExpression n) {
      return Function.returnsString("left(#{}, #{})", text, n);
    }
  
    public StringFunction left(String text, NumberExpression n) {
      return left(BoxUtil.box(text), n);
    }
  
    public StringFunction left(StringExpression text, Number n) {
      return left(text, BoxUtil.box(n));
    }
  
    public StringFunction left(String text, Number n) {
      return left(BoxUtil.box(text), BoxUtil.box(n));
    };
    ```
    
- **Example #6:** Function `format(text, args...)` with one positional parameter and a vararg.

  It includes boxed and unboxed variations for the first parameter only. It
  assumes the second will always be boxed:

    ```java
    public StringFunction format(StringExpression text, Expression... args) {
      return Function.returnsString("format(#{}#{, ?})", Function.bundle(text, args));
    }
  
    public StringFunction format(final String text, final Expression... args) {
      return format(BoxUtil.box(text), args);
    }
    ```

- **Example #7:** Function `set_byte(string, offset, newvalue)` with three positional parameters.

  The example below considers only the variation where all parameters are unboxed. 
  All other seven variations should probably be implemented as in the "left" function above, in order to accept any combination of boxed and unboxed parameters:

    ```java
    public ByteArrayFunction set_byte(byte[] string, Number offset, Number newValue) {
      return Function.returnsByteArray("set_byte(#{}, #{}, #{})",
          Function.bundle(Function.bundle(BoxUtil.box(string), BoxUtil.box(offset)), BoxUtil.box(newValue)));
    }
    ```

