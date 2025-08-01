package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Mult extends BinaryNumericExpression<GeneralNumericExpression> {

  public Mult(final GeneralNumericExpression a, final GeneralNumericExpression b) {
    super(a, "*", b, Expression.PRECEDENCE_MULT_DIV_MOD);
  }

}
