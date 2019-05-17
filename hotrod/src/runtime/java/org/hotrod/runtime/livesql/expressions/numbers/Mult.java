package org.hotrod.runtime.livesql.expressions.numbers;

public class Mult extends BinaryNumberExpression {

  private static final int PRECEDENCE = 3;

  public Mult(final NumberExpression a, final NumberExpression b) {
    super(a, "*", b, PRECEDENCE);
  }

}
