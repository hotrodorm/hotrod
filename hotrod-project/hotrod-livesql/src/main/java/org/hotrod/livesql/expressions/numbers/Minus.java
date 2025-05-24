package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.Expression;

public class Minus extends BinaryNumberExpression<GeneralNumberExpression> {

  public Minus(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "-", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
