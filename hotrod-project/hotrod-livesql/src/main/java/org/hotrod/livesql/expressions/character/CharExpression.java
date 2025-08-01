package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class CharExpression extends GeneralCharExpression {

  protected CharExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
