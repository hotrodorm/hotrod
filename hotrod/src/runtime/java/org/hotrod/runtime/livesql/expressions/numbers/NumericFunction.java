package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class NumericFunction extends NumberExpression {

  protected NumericFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
