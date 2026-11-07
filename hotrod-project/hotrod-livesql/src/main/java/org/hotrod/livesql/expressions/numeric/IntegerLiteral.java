package org.hotrod.livesql.expressions.numeric;

import java.math.BigInteger;

import org.hotrod.livesql.expressions.Expression;

public class IntegerLiteral extends NumericLiteral {

  private static final IntegerLiteral ZERO = new IntegerLiteral(0);
  private static final IntegerLiteral ONE = new IntegerLiteral(1);

  // Constructor

  public IntegerLiteral(final long value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

  public static IntegerLiteral of(final long value) {
    if (value == 0) {
      return ZERO;
    } else if (value == 1) {
      return ONE;
    } else {
      return new IntegerLiteral(value);
    }
  }

  public IntegerLiteral(final BigInteger value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

  public static IntegerLiteral of(final BigInteger value) {
    if (value.equals(BigInteger.ZERO)) {
      return ZERO;
    } else if (value.equals(BigInteger.ONE)) {
      return ONE;
    } else {
      return new IntegerLiteral(value);
    }
  }
  // Getters

  public static IntegerLiteral getZero() {
    return ZERO;
  }

  public static IntegerLiteral getOne() {
    return ONE;
  }

}
