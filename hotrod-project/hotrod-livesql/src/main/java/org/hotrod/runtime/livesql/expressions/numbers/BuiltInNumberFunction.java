package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class BuiltInNumberFunction extends NumberExpression {

  protected BuiltInNumberFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
