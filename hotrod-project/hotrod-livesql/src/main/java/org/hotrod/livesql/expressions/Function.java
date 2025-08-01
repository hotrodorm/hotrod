package org.hotrod.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class Function {

  public static NumericFunction returnsNumber(final String pattern, final ComparableExpression... parameters) {
    return new NumericFunction(pattern, parameters) {
    };
  }

  public static CharFunction returnsString(final String pattern, final ComparableExpression... parameters) {
    return new CharFunction(pattern, parameters) {
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

  public static BinaryFunction returnsByteArray(final String pattern, final ComparableExpression... parameters) {
    return new BinaryFunction(pattern, parameters) {
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
