package org.hotrod.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.numbers.NumberFunction;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.hotrod.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class Function {

  public static NumberFunction returnsNumber(final String pattern, final ComparableExpression... parameters) {
    return new NumberFunction(pattern, parameters) {
    };
  }

  public static StringFunction returnsString(final String pattern, final ComparableExpression... parameters) {
    return new StringFunction(pattern, parameters) {
    };
  }

  public static DateTimeFunction returnsDateTime(final String pattern, final ComparableExpression... parameters) {
    return new DateTimeFunction(pattern, parameters) {
    };
  }

  public static BooleanFunction returnsBoolean(final String pattern, final ComparableExpression... parameters) {
    return new BooleanFunction(pattern, parameters) {
    };
  }

  public static ByteArrayFunction returnsByteArray(final String pattern, final ComparableExpression... parameters) {
    return new ByteArrayFunction(pattern, parameters) {
    };
  }

  public static ObjectFunction returnsObject(final String pattern, final ComparableExpression... parameters) {
    return new ObjectFunction(pattern, parameters) {
    };
  }

  public static ComparableExpression[] bundle(final ComparableExpression a, final ComparableExpression... b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

  public static ComparableExpression[] bundle(final ComparableExpression[] a, final ComparableExpression b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

}
