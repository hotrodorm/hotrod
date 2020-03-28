package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public class GreaterThan extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public <T> GreaterThan(final Expression<T> a, final Expression<T> b) {
    super(a, ">", b, PRECEDENCE);
  }

}
