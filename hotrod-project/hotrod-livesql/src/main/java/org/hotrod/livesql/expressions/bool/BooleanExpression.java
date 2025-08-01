package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class BooleanExpression extends GeneralBooleanExpression {

  protected BooleanExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
