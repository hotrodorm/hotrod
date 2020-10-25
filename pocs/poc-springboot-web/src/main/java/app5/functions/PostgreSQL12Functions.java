package app5.functions;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Function;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.util.BoxUtil;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQL12Functions {

  // The abstract function classes must receive parameters that extend Expression.
  // Typically these are of the types:
  // - NumberExpression
  // - StringExpression
  // - DateTimeExpression
  // - Predicate
  // - ByteArrayExpression
  // - ObjectExpression
  // - Expression (any expression type)

  // Simpler parameters like int, String, etc. can be promoted to Expression
  // in a LiveSQL query using: sql.val(Number|String|Date|Boolean|byte[]|Object)
  // Or by boxing it using BoxUtil.box(x), as shown in the sin() function in this
  // example.

  // random()

  public NumberFunction random() {
    return Function.returnsNumber("random");
  }

  // sin(x)

  public NumberFunction sin(final NumberExpression x) {
    return Function.returnsNumber("sin", x);
  }

  public NumberFunction sin(final Number x) {
    return Function.returnsNumber("sin", BoxUtil.box(x));
  }

  // left(text, n)

  public StringFunction left(final StringExpression text, final NumberExpression n) {
    return Function.returnsString("left", text, n);
  }

  public StringFunction left(final String text, final NumberExpression n) {
    return Function.returnsString("left", BoxUtil.box(text), n);
  }

  public StringFunction left(final StringExpression text, final Number n) {
    return Function.returnsString("left", text, BoxUtil.box(n));
  }

  public StringFunction left(final String text, final Number n) {
    return Function.returnsString("left", BoxUtil.box(text), BoxUtil.box(n));
  };

  // format(text, args...)

  public StringFunction format(final StringExpression text, final Expression... args) {
    return Function.returnsString("format", Function.join(text, args));
  }

  public StringFunction format(final String text, final Expression... args) {
    return Function.returnsString("format", Function.join(BoxUtil.box(text), args));
  }

  // localtimestamp

  public DateTimeFunction localtimestamp() {
    return Function.returnsDateTime("localtimestamp", false);
  };

  // set_byte(string, offset, newvalue)

  // All other seven variations should probably be implemented as in the "left"
  // function above

  public ByteArrayFunction set_byte(final byte[] string, final Number offset, final Number newValue) {
    return Function.returnsByteArray("set_byte",
        Function.join(Function.join(BoxUtil.box(string), BoxUtil.box(offset)), BoxUtil.box(newValue)));
  }
  
  

}
