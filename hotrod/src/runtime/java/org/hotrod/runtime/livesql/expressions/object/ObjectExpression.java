package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class ObjectExpression extends Expression<Object> {

  protected ObjectExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ObjectExpression coalesce(final ObjectExpression a) {
    return new ObjectCoalesce(this, a);
  }

  public ObjectExpression coalesce(final Object a) {
    return new ObjectCoalesce(this, new ObjectConstant(a));
  }

}
