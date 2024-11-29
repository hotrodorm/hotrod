package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Plus extends BinaryNumberExpression<GeneralNumberExpression> {

  public Plus(final GeneralNumberExpression a, final GeneralNumberExpression b) {
    super(a, "+", b, Expression.PRECEDENCE_PLUS_MINUS);
  }

}
