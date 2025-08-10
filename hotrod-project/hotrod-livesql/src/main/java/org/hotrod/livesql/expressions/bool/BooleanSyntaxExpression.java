package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class BooleanSyntaxExpression extends BooleanExpression {

  protected BooleanSyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
