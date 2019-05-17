package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Plus extends BinaryNumberExpression {

  public Plus(final NumberExpression a, final NumberExpression b) {
    super(a, "+", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
