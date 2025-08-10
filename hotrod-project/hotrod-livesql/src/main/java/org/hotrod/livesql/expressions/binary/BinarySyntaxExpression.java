package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class BinarySyntaxExpression extends BinaryExpression {

  protected BinarySyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
