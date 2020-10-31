package app5.functions;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Function;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.util.BoxUtil;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQL12Functions {

  // random()

  public NumberFunction random() {
    return Function.returnsNumber("random()");
  }

  // user-defined function "addone(x)"

  public NumberFunction addone(NumberExpression x) {
    return Function.returnsNumber("addone(#{})", x);
  }

  public NumberFunction addone(Number x) {
    return addone(BoxUtil.box(x));
  }

  // sin(x)

  public NumberFunction sin(final NumberExpression x) {
    return Function.returnsNumber("sin(#{})", x);
  }

  public NumberFunction sin(final Number x) {
    return sin(BoxUtil.box(x));
  }

  // coalesce(a, ...) -- for all six types

  public NumberFunction coalesce(final NumberExpression a, final NumberExpression... b) {
    return Function.returnsNumber("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  public StringFunction coalesce(final StringExpression a, final StringExpression... b) {
    return Function.returnsString("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  public DateTimeFunction coalesce(final DateTimeExpression a, final DateTimeExpression... b) {
    return Function.returnsDateTime("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  public BooleanFunction coalesce(final Predicate a, final Predicate... b) {
    return Function.returnsBoolean("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  public ByteArrayFunction coalesce(final ByteArrayExpression a, final ByteArrayExpression... b) {
    return Function.returnsByteArray("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  public ObjectFunction coalesce(final ObjectExpression a, final ObjectExpression... b) {
    return Function.returnsObject("coalesce(#{?, ?})", Function.bundle(a, b));
  }

  // left(text, n)

  public StringFunction left(final StringExpression text, final NumberExpression n) {
    return Function.returnsString("left(#{}, #{})", text, n);
  }

  public StringFunction left(final String text, final NumberExpression n) {
    return left(BoxUtil.box(text), n);
  }

  public StringFunction left(final StringExpression text, final Number n) {
    return left(text, BoxUtil.box(n));
  }

  public StringFunction left(final String text, final Number n) {
    return left(BoxUtil.box(text), BoxUtil.box(n));
  };

  // format(text, args...)

  public StringFunction format(final StringExpression text, final Expression... args) {
    return Function.returnsString("format(#{}#{, ?})", Function.bundle(text, args));
  }

  public StringFunction format(final String text, final Expression... args) {
    return format(BoxUtil.box(text), args);
  }

  // localtimestamp

  public DateTimeFunction localtimestamp() {
    return Function.returnsDateTime("localtimestamp");
  };

  // set_byte(string, offset, newvalue)

  // All other seven variations should probably be implemented as in the "left"
  // function above

  public ByteArrayFunction set_byte(final byte[] string, final Number offset, final Number newValue) {
    return Function.returnsByteArray("set_byte(#{}, #{}, #{})",
        Function.bundle(Function.bundle(BoxUtil.box(string), BoxUtil.box(offset)), BoxUtil.box(newValue)));
  }

}
