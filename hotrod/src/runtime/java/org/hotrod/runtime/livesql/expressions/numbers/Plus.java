package org.hotrod.runtime.livesql.expressions.numbers;

public class Plus extends BinaryNumberExpression {

  private static final int PRECEDENCE = 4;

  public Plus(final NumberExpression a, final NumberExpression b) {
    super(a, "+", b, PRECEDENCE);
  }

}
