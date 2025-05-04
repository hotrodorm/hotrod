package org.hotrod.runtime.livesql.expressions.predicates;

public abstract class Predicate extends BooleanExpression {

  protected Predicate(int precedence) {
    super(precedence);
  }

}
