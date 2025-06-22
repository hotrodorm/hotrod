package org.hotrod.livesql.expressions.object;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class ObjectExpression extends GeneralObjectExpression {

  protected ObjectExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
