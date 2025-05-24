package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInNumberFunction extends NumberExpression {

  protected BuiltInNumberFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
