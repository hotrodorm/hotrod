package org.hotrod.runtime.livesql.expressions.predicates;

public abstract class BooleanExpression extends GeneralBooleanExpression {

  protected BooleanExpression(int precedence) {
    super(precedence);
  }

}
