package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public class Plus extends BinaryNumericExpression<GeneralNumericExpression> {

  public Plus(final GeneralNumericExpression a, final GeneralNumericExpression b) {
    super(a, "+", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
