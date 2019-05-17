package org.hotrod.runtime.livesql.expressions.numbers;

public class Remainder extends BinaryNumberExpression {

  private static final int PRECEDENCE = 3;

  public Remainder(final NumberExpression a, final NumberExpression b) {
    super(a, "%", b, PRECEDENCE);
  }

}
