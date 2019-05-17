package org.hotrod.runtime.livesql.expressions.numbers;

public class Div extends BinaryNumberExpression {

  private static final int PRECEDENCE = 3;

  public Div(final NumberExpression a, final NumberExpression b) {
    super(a, "/", b, PRECEDENCE);
  }

  
}
