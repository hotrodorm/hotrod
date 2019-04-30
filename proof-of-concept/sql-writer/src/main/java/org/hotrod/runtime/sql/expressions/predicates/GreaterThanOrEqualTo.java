package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public class GreaterThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public <T> GreaterThanOrEqualTo(final Expression<T> a, final Expression<T> b) {
    super(a, ">=", b, PRECEDENCE);
  }

}
