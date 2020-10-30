# Creating Custom Functions

LiveSQL includes the infrastructure to add custom database functions. These can be built-in database functions, or user-defined functions available in the database.

To add new functions, create a `@Component` that include methods for each function. For example:

    @Component
    public class PostgreSQL12Functions {
    
      public NumberFunction random() {
        return Function.returnsNumber("random()");
      }
      
    }

Then, add it to your class:

    @Autowired
    private PostgreSQL12Functions pg;

And use it:

    sql.select(pg.random()).execute()
    
To produce a result such as: `0.796045811049445`    

Use any of the six factory methods available in the `Function` class to create a function with the corresponding return type:

- `Function.returnsNumber(String pattern, Expression... parameters)` -- to produce a `NumberFunction`
- `Function.returnsString(String pattern, Expression... parameters)` -- to produce a `StringFunction`
- `Function.returnsDateTime(String pattern, Expression... parameters)` -- to produce a `DateTimeFunction`
- `Function.returnsBoolean(String pattern, Expression... parameters)` -- to produce a `BooleanFunction`
- `Function.returnsByteArray(String pattern, Expression... parameters)` -- to produce a `ByteArrayFunction`
- `Function.returnsObject(String pattern, Expression... parameters)` -- to produce a `ObjectFunction`

**Note**: The `CURSOR` data type is not supported, either as an IN/OUT parameter or as a return type.

# Parameters

The abstract function classes must receive parameters that extend `org.hotrod.runtime.livesql.expressions.Expression`. Typically these are of the types:

 - `NumberExpression`
 - `StringExpression`
 - `DateTimeExpression`
 - `Predicate` (boolean expression)
 - `ByteArrayExpression`
 - `ObjectExpression`
 - `Expression` (any expression type)

Notice, the function factory receives either a vararg (`Expression...`) or a *single* array (`Expression[]`). If your function receives multiple separate parameters that you need to assemble as a single array parameter, you can bundle together using `Function.bundle(a, b)`.

## Parameters Boxing

Primitives values and simple parameters like:

 - Any `java.lang.Number` such as `int`, `Integer`, or `Float`
 - `String`
 - `java.util.Date`
 - `boolean` or `Boolean`
 - `byte[]`
 - any `Object`

can be promoted to `Expression`:

 - while typing a LiveSQL query using `sql.val(Number|String|Date|Boolean|byte[]|Object)`, or
 - by offering multiple constructors in the custom function definition and then boxing simple parameters as `Expression` using `BoxUtil.box(x)`, as shown in the `sin()` function below:

        public NumberFunction sin(NumberExpression x) { // already an Expression parameter
          return Function.returnsNumber("sin(#{})", x);
        }
      
        public NumberFunction sin(Number x) { // accepts any Number
          return sin(BoxUtil.box(x));
        }

This way, the above function can be called with an Expression as in `sin(a.angle)`, or with a primitive value as in `sin(123)`.

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

    create function addone(in a integer) returns integer
    as $bodytag$
    begin
      return a + 1;
    end;
    $bodytag$ language plpgsql;
    //

A class could be used to provide the function to LiveSQL. For example:

    @Component
    public class MyFunctions {
    
      public NumberFunction addone(NumberExpression a) {
        return Function.returnsNumber("addone(#{})", a);
      }
    
      public NumberFunction addone(Number a) {
        return addone(BoxUtil.box(a));
      }
      
    }

and could be used in the LiveSQL query, as in:

    @Autowired
    private MyFunctions mf;
    
    ...
    
    sql.select(mf.addone(123)).execute()
    
To produce the result `124`.


    


 