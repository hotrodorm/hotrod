package org.hotrod.runtime.livesql.expressions;

public abstract class OrderingTerm extends Expression {

  protected OrderingTerm(final int precedence) {
    super(precedence);
  }

}
