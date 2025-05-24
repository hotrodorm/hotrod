package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class StringExpression extends GeneralStringExpression {

  protected StringExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
