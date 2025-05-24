package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class NumberExpression extends GeneralNumberExpression {

  protected NumberExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
