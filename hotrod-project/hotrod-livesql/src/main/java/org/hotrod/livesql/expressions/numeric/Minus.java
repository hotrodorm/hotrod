package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Minus extends BinaryNumericExpression<NumericExpression> {

  public Minus(final NumericExpression a, final NumericExpression b) {
    super(a, "-", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
