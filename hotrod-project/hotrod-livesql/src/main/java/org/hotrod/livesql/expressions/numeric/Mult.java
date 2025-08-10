package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Mult extends BinaryNumericExpression<NumericExpression> {

  public Mult(final NumericExpression a, final NumericExpression b) {
    super(a, "*", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
