package org.hotrod.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.livesql.expressions.binary.CustomBinaryFunction;
import org.hotrod.livesql.expressions.bool.CustomBooleanFunction;
import org.hotrod.livesql.expressions.character.CustomCharFunction;
import org.hotrod.livesql.expressions.datetime.CustomDateTimeFunction;
import org.hotrod.livesql.expressions.numeric.CustomNumericFunction;
import org.hotrod.livesql.expressions.object.CustomObjectFunction;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;

public class Function {

  public static CustomNumericFunction returnsNumeric(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomNumericFunction(template, parameters) {
    };
  }

  public static CustomCharFunction returnsChar(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomCharFunction(template, parameters) {
    };
  }

  public static CustomDateTimeFunction returnsDateTime(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomDateTimeFunction(template, parameters) {
    };
  }

  public static CustomBooleanFunction returnsBoolean(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomBooleanFunction(template, parameters) {
    };
  }

  public static CustomBinaryFunction returnsBinary(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomBinaryFunction(template, parameters) {
    };
  }

  public static CustomObjectFunction returnsObject(final LiveSQLFunctionTemplate template,
      final ComparableExpression... parameters) {
    return new CustomObjectFunction(template, parameters) {
    };
  }

  public static ComparableExpression[] bundle(final ComparableExpression a, final ComparableExpression... b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

  public static ComparableExpression[] bundle(final ComparableExpression[] a, final ComparableExpression b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(ComparableExpression[]::new);
  }

}
