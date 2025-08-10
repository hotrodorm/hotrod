package org.hotrod.livesql.expressions.object;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class ObjectSyntaxExpression extends ObjectExpression {

  protected ObjectSyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
