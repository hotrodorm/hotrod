package org.hotrod.runtime.livesql.expressions;

import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class Function {

  public static NumberFunction returnsNumber(final String name, final Expression... parameters) {
    return new NumberFunction(name, parameters) {
    };
  }

  public static NumberFunction returnsNumber(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new NumberFunction(name, parenthesis, parameters) {
    };
  }

  public static StringFunction returnsString(final String name, final Expression... parameters) {
    return new StringFunction(name, parameters) {
    };
  }

  public static StringFunction returnsString(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new StringFunction(name, parenthesis, parameters) {
    };
  }

  public static DateTimeFunction returnsDateTime(final String name, final Expression... parameters) {
    return new DateTimeFunction(name, parameters) {
    };
  }

  public static DateTimeFunction returnsDateTime(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new DateTimeFunction(name, parenthesis, parameters) {
    };
  }

  public static BooleanFunction returnsBoolean(final String name, final Expression... parameters) {
    return new BooleanFunction(name, parameters) {
    };
  }

  public static BooleanFunction returnsBoolean(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new BooleanFunction(name, parenthesis, parameters) {
    };
  }

  public static ByteArrayFunction returnsByteArray(final String name, final Expression... parameters) {
    return new ByteArrayFunction(name, parameters) {
    };
  }

  public static ByteArrayFunction returnsByteArray(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new ByteArrayFunction(name, parenthesis, parameters) {
    };
  }

  public static ObjectFunction returnsObject(final String name, final Expression... parameters) {
    return new ObjectFunction(name, parameters) {
    };
  }

  public static ObjectFunction returnsObject(final String name, final boolean parenthesis,
      final Expression... parameters) {
    return new ObjectFunction(name, parenthesis, parameters) {
    };
  }

  public static Expression[] join(final Expression a, final Expression... b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(Expression[]::new);
  }

  public static Expression[] join(final Expression[] a, final Expression b) {
    return Stream.concat(Stream.of(a), Stream.of(b)).toArray(Expression[]::new);
  }

}
