package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Minus extends BinaryNumberExpression<GeneralNumberExpression> {

  public Minus(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "-", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
