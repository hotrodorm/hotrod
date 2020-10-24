package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Minus extends BinaryNumberExpression<NumberExpression> {

  public Minus(final NumberExpression a, final NumberExpression b) {
    super(a, "-", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
