package org.hotrod.runtime.livesql.expressions.numbers;

public class Minus extends BinaryNumberExpression {

  private static final int PRECEDENCE = 4;

  public Minus(final NumberExpression a, final NumberExpression b) {
    super(a, " - ", b, PRECEDENCE);
  }

}
