package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class ObjectExpression extends Expression<Object> {

  protected ObjectExpression(final int precedence) {
    super(precedence);
  }

}
