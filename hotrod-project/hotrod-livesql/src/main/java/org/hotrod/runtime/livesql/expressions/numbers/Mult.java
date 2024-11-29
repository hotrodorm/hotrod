package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Mult extends BinaryNumberExpression<GeneralNumberExpression> {

  public Mult(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "*", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
