package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Minus extends BinaryNumericExpression<GeneralNumericExpression> {

  public Minus(final GeneralNumericExpression a, final GeneralNumericExpression b) {
    super(a, "-", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
