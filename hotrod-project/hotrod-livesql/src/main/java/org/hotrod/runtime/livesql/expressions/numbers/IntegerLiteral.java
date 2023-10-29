package org.hotrod.runtime.livesql.expressions.numbers;

import java.math.BigInteger;

import org.hotrod.runtime.livesql.expressions.Expression;

public class IntegerLiteral extends NumericLiteral {

  private static final IntegerLiteral ZERO = new IntegerLiteral(0);
  private static final IntegerLiteral ONE = new IntegerLiteral(1);

  // Constructor

  public IntegerLiteral(final long value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

  public IntegerLiteral(final BigInteger value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

  // Getters

  public static IntegerLiteral getZero() {
    return ZERO;
  }

  public static IntegerLiteral getOne() {
    return ONE;
  }

}
