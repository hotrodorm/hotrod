package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.expressions.Expression;

public class LessThanOrEqualTo extends BinaryPredicate {

  private static final int PRECEDENCE = 6;

  public <T> LessThanOrEqualTo(final Expression<T> a, final Expression<T> b) {
    super(a, "<=", b, PRECEDENCE);
  }

}
