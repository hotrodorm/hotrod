package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Plus extends BinaryNumericExpression<NumericExpression> {

  public Plus(final NumericExpression a, final NumericExpression b) {
    super(a, "+", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
