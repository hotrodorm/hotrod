package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.Expression;

public class Div extends BinaryNumberExpression<GeneralNumberExpression> {

  public Div(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "/", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
