package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.livesql.expressions.bool.BooleanExpression;

@Deprecated
public abstract class Predicate extends BooleanExpression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

}
