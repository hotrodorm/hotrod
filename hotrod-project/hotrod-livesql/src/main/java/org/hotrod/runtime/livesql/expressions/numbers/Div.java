package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Div extends BinaryNumberExpression<GeneralNumberExpression> {

  public Div(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "/", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
