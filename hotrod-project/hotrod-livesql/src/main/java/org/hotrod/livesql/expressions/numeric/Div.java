package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Div extends BinaryNumericExpression<NumericExpression> {

  public Div(final NumericExpression a, final NumericExpression b) {
    super(a, "/", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
