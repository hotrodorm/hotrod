package org.hotrod.runtime.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class Function {

  public static NumberFunction returnsNumber(final String pattern, final Expression... parameters) {
    return new NumberFunction(pattern, parameters) {
    };
  }

  public static StringFunction returnsString(final String pattern, final Expression... parameters) {
    return new StringFunction(pattern, parameters) {
    };
  }

  public static DateTimeFunction returnsDateTime(final String pattern, final Expression... parameters) {
    return new DateTimeFunction(pattern, parameters) {
    };
  }

  public static BooleanFunction returnsBoolean(final String pattern, final Expression... parameters) {
    return new BooleanFunction(pattern, parameters) {
    };
  }

  public static ByteArrayFunction returnsByteArray(final String pattern, final Expression... parameters) {
    return new ByteArrayFunction(pattern, parameters) {
    };
  }

  public static ObjectFunction returnsObject(final String pattern, final Expression... parameters) {
    return new ObjectFunction(pattern, parameters) {
    };
  }

  public static Expression[] bundle(final Expression a, final Expression... b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(Expression[]::new);
  }

  public static Expression[] bundle(final Expression[] a, final Expression b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(Expression[]::new);
  }

}
