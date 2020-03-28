package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class Predicate extends Expression<Boolean> {

  protected Predicate(final int precedence) {
    super(precedence);
  }

}
