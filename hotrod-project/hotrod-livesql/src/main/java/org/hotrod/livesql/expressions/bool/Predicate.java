package org.hotrod.livesql.expressions.bool;

public abstract class Predicate extends BooleanExpression {

  protected Predicate(int precedence) {
    super(precedence);
  }

}
