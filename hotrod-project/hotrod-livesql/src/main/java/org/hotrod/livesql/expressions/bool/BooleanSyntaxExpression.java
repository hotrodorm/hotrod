package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.TypedExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class BooleanSyntaxExpression extends Predicate {

  protected BooleanSyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
