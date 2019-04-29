package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public abstract class Predicate extends Expression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

}
