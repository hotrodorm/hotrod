package org.hotrod.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.livesql.expressions.binary.CustomBinaryFunction;
import org.hotrod.livesql.expressions.bool.CustomBooleanFunction;
import org.hotrod.livesql.expressions.character.CustomCharFunction;
import org.hotrod.livesql.expressions.datetime.CustomDateTimeFunction;
import org.hotrod.livesql.expressions.numeric.CustomNumericFunction;
import org.hotrod.livesql.expressions.object.CustomObjectFunction;

public class Function {

  public static CustomNumericFunction returnsNumeric(final String pattern, final ComparableExpression... parameters) {
    return new CustomNumericFunction(pattern, parameters) {
    };
  }

  public static CustomCharFunction returnsChar(final String pattern, final ComparableExpression... parameters) {
    return new CustomCharFunction(pattern, parameters) {
    };
  }

  public static CustomDateTimeFunction returnsDateTime(final String pattern, final ComparableExpression... parameters) {
    return new CustomDateTimeFunction(pattern, parameters) {
    };
  }

  public static CustomBooleanFunction returnsBoolean(final String pattern, final ComparableExpression... parameters) {
    return new CustomBooleanFunction(pattern, parameters) {
    };
  }

  public static CustomBinaryFunction returnsBinary(final String pattern, final ComparableExpression... parameters) {
    return new CustomBinaryFunction(pattern, parameters) {
    };
  }

  public static CustomObjectFunction returnsObject(final String pattern, final ComparableExpression... parameters) {
    return new CustomObjectFunction(pattern, parameters) {
    };
  }

  public static ComparableExpression[] bundle(final ComparableExpression a, final ComparableExpression... b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

  public static ComparableExpression[] bundle(final ComparableExpression[] a, final ComparableExpression b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

}
