package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Remainder extends BinaryNumberExpression<NumberExpression> {

  public Remainder(final NumberExpression a, final NumberExpression b) {
    super(a, "%", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
