package app5.functions;

import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringConstant;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQL_12_Functions {

  // According to its returning type, a function can extend:
  // - NumberFunction
  // - StringFunction
  // - DateTimeFunction
  // - BooleanFunction
  // - ByteArrayFunction
  // - ObjectFunction

  // The abstract function classes must receive parameters that extend Expression.
  // Typically these are of the types:
  // - Expression (generic type)
  // - NumberExpression
  // - StringExpression
  // - DateTimeExpression
  // - Predicate (aka BooleanExpression)
  // - ByteArrayExpression
  // - ObjectExpression

  // Simpler parameters like int, String, etc. can be promoted to Expression
  // in a LiveSQL query using: sql.val(Number|String|Date|Boolean|byte[]|Object)
  // Or by providing specialized constructors in the function, as shown in the
  // sin() function in this example:
  // - new NumberConstant(Number)
  // - new StringConstant(String)
  // - new DateTimeConstant(java.util.Date)
  // - new BooleanConstant(Boolean)
  // - new ByteArrayConstant(byte[])
  // - new ObjectConstant(Object)

  // random()

  public NumberFunction random() {
    return new NumberFunction("random") {
    };
  }

  // sin(x)

  public NumberFunction sin(final NumberExpression x) {
    return new NumberFunction("sin", x) {
    };
  }

  public NumberFunction sin(final Number x) {
    return sin(new NumberConstant(x));
  }

  // left(text, n)

  public StringFunction left(final StringExpression text, final NumberExpression n) {
    return new StringFunction("left", text, n) {
    };
  }

  public StringFunction left(final String text, final NumberExpression n) {
    return left(new StringConstant(text), n);
  }

  public StringFunction left(final StringExpression text, final Number n) {
    return left(text, new NumberConstant(n));
  }

  public StringFunction left(final String text, final Number n) {
    return left(new StringConstant(text), new NumberConstant(n));
  };

  // format(text, args...)

  public StringFunction format(final StringExpression text, final Expression... args) {
    return new StringFunction("format", Stream.concat(Stream.of(text), Stream.of(args))) {
    };
  }

  public StringFunction format(final String text, final Expression... args) {
    return format(new StringConstant(text), args);
  }

}
